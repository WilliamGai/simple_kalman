package baoutil;

import java.awt.Color;

public class LogB {
	private static LogB LogB = null;
	private static LogBatJFrame jFrame = null;
	private LogB(){
		jFrame = new LogBatJFrame("william");
	}
	public static LogB getInstance(){
		if(LogB==null){
			LogB = new LogB();
		}
		return LogB;
	}
	public static void i(String s){
		LogB.getInstance();
		jFrame.setDocs(getTraceInfo()+s, Color.green, null, null, null);
	}
	public static void i(String str, Color col, Color b_col, Boolean bold,
			Integer fontSize){
		LogB.getInstance();
		jFrame.setDocs(str, col, b_col, bold, fontSize);
	}

	public static void i(String str, Color col) {
		LogB.getInstance();
		jFrame.setDocs(str, col, null, null, null);
	}
	public static String getTraceInfo(){  
        StringBuffer sb = new StringBuffer();   
          
        StackTraceElement[] stacks = new Throwable().getStackTrace();  
        String className = stacks[1].getClassName();
        String methodName = stacks[1].getMethodName();
        String lineNum = stacks[1].getLineNumber()+"";
        
        className = addSpace(className,4);
        methodName = addSpace(methodName,4);
        lineNum = addSpace(lineNum,4);
        
//        int n = 10;
//        while(n-->=0)
        sb.append("~").append(className).append(">").append(methodName).append(">").append(lineNum);  
          
        return sb.toString();  
    }  
	public static String addSpace(String s, int len){
		StringBuffer sb = new StringBuffer();
		sb.append(s);
		for (int i = 0; i < len-s.length(); i++) {
			sb.append(" ");
		}
		return new String(sb);
	}
}
