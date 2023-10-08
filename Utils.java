
import java.util.ArrayList;
import java.util.List;
public class Utils{
	public static String[] textToTJA(String[] qline) {
    	double m1 = 4;
    	double m2 = 4;
    	double bpm = 120;
    	double delay = 0;
    	ArrayList<String> allList = new ArrayList<>();
    	ArrayList<String> numlist = new ArrayList<>();
    	ArrayList<Double> bpmlist = new ArrayList<>();
    	ArrayList<Double> measurelist = new ArrayList<>();
    	ArrayList<String> smeasurelist = new ArrayList<>();
    	ArrayList<Double> delaylist = new ArrayList<>();
    	ArrayList<Boolean> bpmchangelist = new ArrayList<>();
    	ArrayList<Boolean> measurechangelist = new ArrayList<>();
    	ArrayList<Boolean> delaychangelist = new ArrayList<>();
    	ArrayList<Boolean> gogostartlist = new ArrayList<>();
    	ArrayList<Boolean> gogoendlist = new ArrayList<>();
    	ArrayList<Boolean> barlineonlist = new ArrayList<>();
    	ArrayList<Boolean> barlineofflist = new ArrayList<>();
    	ArrayList<ArrayList<String>> commentlist = new ArrayList<>();
    	double startbpm = 0;
    	double endbpm = 0;
    	int howto = 1;
    	boolean startjudge = false;
    	int up = 0;
    	while(true){
    		String s = qline[up];
    		if(s.startsWith("#BPM:")) bpm = Double.parseDouble(s.substring(5));
    		if(s.startsWith("#startBPM:")) startbpm = Double.parseDouble(s.substring(10));
    		if(s.startsWith("#endBPM:")) endbpm = Double.parseDouble(s.substring(8));
    		if(s.startsWith("#howto:")) howto = Integer.parseInt(s.substring(7));
    		if(s.startsWith("#START")) startjudge = true;
    		if(startjudge && !s.startsWith("#SCROLL")) allList.add(s);
    		if(s.startsWith("#END")) break;
    		up++;
    	}
    	ArrayList<String> nallList = new ArrayList<>();
    	for(int i = 0; i < allList.size(); i++){
    		String s = allList.get(i);
    		if(s.contains("//")){
    			String s1 = s.substring(0,s.indexOf("//"));
    			String s2 = s.substring(s.indexOf("//"));
    			s1 = clean(s1);
    			nallList.add(s2);
    			if(!s1.equals(""))nallList.add(s1);
    		}
    		else nallList.add(s);
    	}
    	ArrayList<String> comment = new ArrayList<>();
    	//bpm;
    	boolean bpmchange = false;
    	//m1/m2
    	boolean measurechange = false;
    	boolean delaychange = false;
    	boolean gogostart = false;
    	boolean gogoend = false;
    	boolean barlineon = false;
    	boolean barlineoff = false;
    	//delay
    	for(int i = 0; i < nallList.size(); i++){
    		String s = nallList.get(i);
    		boolean judge = false;
    		if(s.startsWith("//")) {
    			comment.add(s);
    			judge = true;
    		}
    		else if(s.startsWith("#BPMCHANGE")){
    			bpm = Double.parseDouble(s.substring(10));
    			bpmchange = true;
    			judge = true;
    		}
    		else if(s.startsWith("#MEASURE")){
    			m1 = Double.parseDouble(s.substring(9,s.indexOf("/")));
    			m2 = Double.parseDouble(s.substring(s.indexOf("/") + 1));
    			measurechange = true;
    			judge = true;
    		}
    		else if(s.startsWith("#GOGOSTART")){
    			gogostart = true;
    			judge = true;
    		}
    		else if(s.startsWith("#GOGOEND")){
    			gogoend = true;
    			judge = true;
    		}
    		else if(s.startsWith("#BARLINEON")){
    			barlineon = true;
    			judge = true;
    		}
    		else if(s.startsWith("#BARLINEOFF")){
    			barlineoff = true;
    			judge = true;
    		}
    		else if(s.startsWith("#DELAY")){
    			delay = Double.parseDouble(s.substring(6));
    			delaychange = true;
    			judge = true;
    		}
    		if(judge) continue;
    		for(int j = 0; j < s.length(); j++){
    			String str = s.substring(j,j + 1);
    			if(numhan(str)){
    				commentlist.add(comment);
    				ArrayList<String> newlist = new ArrayList<>();
    				comment = newlist;
    				bpmlist.add(bpm);
    				bpmchangelist.add(bpmchange);
    				bpmchange = false;
    				measurelist.add(m1/m2);
    				smeasurelist.add((int)m1 + "/" + (int)m2 + "");
    				measurechangelist.add(measurechange);
    				measurechange = false;
    				delaychangelist.add(delaychange);
    				delaychange = false;
    				gogostartlist.add(gogostart);
    				gogostart = false;
    				gogoendlist.add(gogoend);
    				gogoend = false;
    				barlineonlist.add(barlineon);
    				barlineon = false;
    				barlineofflist.add(barlineoff);
    				barlineoff = false;
    				delaylist.add(delay);
    			}
    		}
    		
    	}
    	ArrayList<String> tlist = new ArrayList<>();
    	ArrayList<Double> tclist = new ArrayList<>();
    	String a = "";
    	ArrayList<Boolean> tracklist = new ArrayList<>();
    	boolean track = true;
    	for(int i = 0; i < nallList.size(); i++){
    		if(!numhan(nallList.get(i))) continue;
    		for(int j = 0; j < nallList.get(i).length(); j++){
    			if(numhan(nallList.get(i).substring(j,j + 1))) {
    				a+= nallList.get(i).substring(j,j + 1);
    				tracklist.add(track);
    				track = false;
    			}
    			else if(nallList.get(i).substring(j,j + 1).equals(",")){
    				tlist.add(a + ",");
    				tclist.add((double)a.length());
    				a = "";
    				track = true;
    			}
    		}
    	}
    	ArrayList<Double> blist = new ArrayList<>();
    	for(int i = 0; i < tlist.size(); i++){
    		for(int j = 0; j < tlist.get(i).length() - 1; j++){
    			blist.add(1.0/tclist.get(i));
    		}
    	}
    	ArrayList<Double> slist = new ArrayList<>();
    	double second = 0;
    	for(int i = 0; i < bpmlist.size(); i++){
    		double per = blist.get(i) * (240.0/bpmlist.get(i)) * measurelist.get(i);
    		slist.add(second + delaylist.get(i));
    		second += per;
    	}
    	second = Math.max(second,second + delaylist.get(delaylist.size() - 1));
    	double diff = endbpm - startbpm;
    	double diff2 = endbpm/startbpm;
    	ArrayList<Double> scList = new ArrayList<>();
    	ArrayList<Double> scList2 = new ArrayList<>();
    	for(int i = 0; i < slist.size(); i++){
    		scList.add((startbpm + diff*(slist.get(i)/second))/bpmlist.get(i));
    		scList2.add((startbpm * Math.pow(diff2,slist.get(i)/second)/bpmlist.get(i)));
    	}
    	if(howto == 2)scList = scList2;
    	int index = 0;
    	ArrayList<String> tjalist = new ArrayList<>();
    	for(int i = 0; i < tlist.size(); i++){
    		for(int j = 0; j < tlist.get(i).length(); j++){
    			String s = tlist.get(i).substring(j,j + 1);
    			if(numhan(s)){
    				for(int k = 0; k < commentlist.get(index).size(); k++){
    					tjalist.add(commentlist.get(index).get(k));
    				}
    				if(delaychangelist.get(index) && delaylist.get(index) < 0){
    					tjalist.add("#DELAY " + delaylist.get(index));
    				}
    				if(bpmchangelist.get(index)){
    					tjalist.add("#BPMCHANGE " + dtos(bpmlist.get(index)));
    				}
    				if(measurechangelist.get(index)){
    					tjalist.add("#MEASURE " + smeasurelist.get(index));
    				}
    				if(gogostartlist.get(index)){
    					tjalist.add("#GOGOSTART");
    				}
    				if(gogoendlist.get(index)){
    					tjalist.add("#GOGOEND");
    				}
    				if(tracklist.get(index) || 
    					delaychangelist.get(index) || 
    					bpmchangelist.get(index) || 
    					measurechangelist.get(index) || 
    					gogostartlist.get(index) || 
    					gogoendlist.get(index) ||
    					barlineonlist.get(index) ||
    					barlineofflist.get(index) ||
    					!s.equals("0")
    				)tjalist.add("#SCROLL " + scList.get(index));
    				if(barlineonlist.get(index)){
    					tjalist.add("#BARLINEON");
    				}
    				if(barlineofflist.get(index)){
    					tjalist.add("#BARLINEOFF");
    				}
    				if(delaychangelist.get(index) && 0 < delaylist.get(index)){
    					tjalist.add("#DELAY " + delaylist.get(index));
    				}
    				tjalist.add(s);
    				index++;
    			}
    			else tjalist.add(s);
    		}
    	}
    	String str = "";
    	ArrayList<String> goal = new ArrayList<>();
    	for(int i = 0; i < tjalist.size(); i++){
    		String s = tjalist.get(i);
    		if(numhan(s) || s.equals(",")){
    			str += s;
    		}
    		else {
    			if(!str.equals(""))goal.add(str);
    			str = "";
    			goal.add(s);
    		}
    	}
    	if(!str.equals(""))goal.add(str);
    	String line[] = new String[goal.size() + 3];
    	line[0] = "---output.tja---";
    	line[1] = "#START";
    	for(int i = 0; i < goal.size(); i++) line[i + 2] = goal.get(i);
    	line[goal.size() + 2] = "#END";
    	return line;
    }
	public static boolean numhan(String s){
		if(s.startsWith("0") || 
		s.startsWith("1") || 
		s.startsWith("2") || 
		s.startsWith("3") || 
		s.startsWith("4") || 
		s.startsWith("5") || 
		s.startsWith("6") || 
		s.startsWith("7") || 
		s.startsWith("8") || 
		s.startsWith("9")) return true;
		return false;
	}
	public static String clean(String s){
		String ns = "";
		boolean han = false;
		for(int i = 0; i < s.length(); i++){
			String k = s.substring(i,i + 1);
			if(han && (k.equals(" ") || k.equals("@"))) {
				break;
			}
			if(k.equals(" ")) {
				han = true;
				ns += k;
			}
			else ns += k;
		}
		return ns;
	}
	public static String dtos(double a){
		String s = a + "";
		String g1 = "";
		String g2 = "";
		for(int i = 0; i < s.length(); i++){
			if(s.substring(i,i + 1).equals(".")) break;
			g1 += s.substring(i,i + 1);
		}
		boolean han = false;
		boolean han2 = false;
		for(int i = 0; i < s.length(); i++){
			if(han && !s.substring(i,i + 1).equals("0")){
				han2 = true;
			}
			if(s.substring(i,i + 1).equals(".")) han = true;
		}
		if(han2) return s;
		return g1;
	}
}