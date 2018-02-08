import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;

public class CodeCounter {

	public static int codeNum = 0;
	public static int commentNum =0;
	public static int blankNum =0;
	
	
	public static Map<String, String> FileExtentMap = new HashMap<String,String>();
	
	private static String mLangugeExtentRegex ="";
			
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		if(args.length <1)
		{
			System.out.print("Usage:\narg1: Directory or File \n[arg2]: Language=Java[Default]\n" );
			return;
		}
		LoadLanguageExtent();
		
		String lang = "java";
		if(args.length>1)
		{
			lang = args[1];
		}
		System.out.println(lang);
		
		mLangugeExtentRegex ="";
		for (String langreg : FileExtentMap.keySet()) {
			if( Pattern.matches(langreg, lang) )
			{
				mLangugeExtentRegex = FileExtentMap.get(langreg);
				System.out.println("your project is "+ langreg);
				break;
			}
				
		}
		
		
		String path = args[0];
		try {
			FileCount(new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("CodeNumber: "+codeNum);
		System.out.println("CommentNumber: "+commentNum);
		System.out.println("BlankNumber: "+blankNum);
		
		
	}
	
	private static void LoadLanguageExtent()
	{
		FileExtentMap.put("[J|j]ava", ".*[\\.]java$");
		FileExtentMap.put("[c|C]#", ".*[\\.]cs$");
		FileExtentMap.put("[c|C]", ".*[\\.][h|c]$");
		FileExtentMap.put("[c|C](p{2}|\\+{2})", ".*[\\.][h|c]$");
	}
	
	private static void FileCount(File file) throws IOException
	{
		//processing Directory 
		if(file.isDirectory())
		{
			File[] files = file.listFiles();
			for(int i=0; i< files.length; i++)
			{
				FileCount(files[i]);
			}
		}
		else {
			//processing Text File
			String filename = file.getName();
			boolean isTargetFile = Pattern.matches(mLangugeExtentRegex, filename);
			if( ! isTargetFile)
			{
				//System.out.println("exclude file: "+ file.getName());
				return;
			}
				
			BufferedReader br = new BufferedReader(new FileReader(file));
			String str = br.readLine();
			boolean commentState = false;
			while(str !=null)
			{
				if(str.startsWith("/*") && str.endsWith("*/")) {
					commentNum++;
                } else if(str.trim().startsWith("//")) {
                	commentNum++;
                } else if(str.startsWith("/*") && !str.endsWith("*/")) {
                	commentNum++;
                	commentState = true;
                } else if(!str.startsWith("/*") && str.endsWith("*/")) {
                	commentNum++;
                	commentState = false;
                } else if(commentState) {
                	commentNum++;
                } else if(str.trim().length() < 1) {
                    blankNum++;
                } else {
                    codeNum++;
                }
				str = br.readLine();
			}
			
			br.close();
		}
		
	}
	
	

}
