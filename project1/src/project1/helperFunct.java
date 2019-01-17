package project1;

public class helperFunct {
	
	public String lister( String listStr, String URL) {
		String listHtml ="";
		// ran into a problem where some movie titles had ", " in them, which breaks links
		// when we split the string on ",", so below i temporarily replace that char sequence with
		// one that is less likely to ever appear in a name "}@", and than reverse the process once
		// the string has been split
		listStr = listStr.replace(", ", "}@");
		
		String temp[] = listStr.split(",",0);
		
		int i =0;
		while(i<temp.length) {
			
			if(temp[i].contains("}@")) {temp[i]=temp[i].replace("}@",", ");} //reverse the string replacement seen above
			//check if is last string so dont need to append ","
			if(i ==temp.length-1) {
				listHtml += "<a href = \""+URL+"?query=" + temp[i] +"\">" + temp[i] + "</a>";
			}
			else {
				listHtml += "<a href = \""+URL+"?query=" + temp[i] +"\">" + temp[i] + ", "+ "</a>";
			}
			i++;
		}
		return listHtml;
	}

}
