package simula.core.utilities;

public abstract class Html {

//	public static final String styleKeyWord = " style=\"color: rgb(153,0,51);\"";
	public static final String styleKeyWord = " style=\"color: rgb(153,0,51); font-weight: bold;\"";
	public static final String styleComment = " style=\"color: rgb(0,153,153);\"";
	public static final String styleConstant = " style=\"color: rgb(204,153,0);\"";
	
	public static String styledText(final String style, final String text) {
		return "<span " + style + ">"
   			+ text
   			+ "</span>";
	}
	
	public static String edKeyWord(int keyWord) {
		String id = KeyWord.edit(keyWord);
		return styledText(Html.styleKeyWord, id.toLowerCase()); 
	}
	
	/// String phrase:
	///             "<span " + Util.styleComment + ">"
    ///			  + any'String
    ///			  + "</span>"
	public static String edPsi(int lno, int lastLine, String phrase) {
		StringBuilder sb = new StringBuilder();
		if(lno > 0) {
			sb.append("Line ").append(lno);
			if(lastLine > 0 && lastLine != lno) sb.append('-').append(lastLine);
			sb.append(": ");
		}
		sb.append(phrase);
		
		
//    	String html = "<html><b><u><p style=\"color: rgb(153,0,51);\">" + sb + "</p></u></b></html>";
//    	String html = "<html><b><u><p" + styleKeyWord + ">" + sb + "</p></u></b></html>";
//    	String html = "<html><b><u><p" + styleKeyWord + ">" + sb + "</p><p" + styleComment + ">" + sb + "</p></u></b></html>";
		
//    	String html = "<html><b>"
//    			+ "<u>"
//    			+ "<p>"
//    			+ "LEAD: "
//    			+ "<span " + Util.styleKeyWord + ">"
//    			+ sb
//    			+ "</span>"
//    			+ " text "
//    			+ "<span " + styleComment + ">"
//    			+ sb
//    			+ "</span>"
//    			+ "</p>"
//    			+ "</u>"
//    			+ "</b>"
//    			+ "</html>";
    					
		
    	String html = "<html>"
//    			+ "<b>"
//    			+ "<u>"
    			+ "<p>"
    			+ sb
    			+ "</p>"
//    			+ "</u>"
//    			+ "</b>"
    			+ "</html>";
    					
//		return sb.toString();
    	return html;
	}


}
