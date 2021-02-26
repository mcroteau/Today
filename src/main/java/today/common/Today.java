package sequence.common;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sequence.model.Activity;
import sequence.model.ProspectActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sequence {

	private static final Logger log = Logger.getLogger(Sequence.class);

	public static int getNumber(int max){
		Random r = new Random();
		return r.nextInt(max);
	}

	public static boolean containsSpecialCharacters(String str) {
		Pattern p = Pattern.compile("[^A-Za-z0-9]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		boolean b = m.find();

		if(b)return true;
		return false;
	}

	public static String getGenericFileName(CommonsMultipartFile file){

		FileItem fileItem = file.getFileItem();
		String originalName = fileItem.getName();

		String fileName = getString(9);
		String extension = getExtension(originalName);

		return fileName + "." +  extension;
	}


	private static String getExtension(final String path) {
		String result = null;
		if (path != null) {
			result = "";
			if (path.lastIndexOf('.') != -1) {
				result = path.substring(path.lastIndexOf('.'));
				if (result.startsWith(".")) {
					result = result.substring(1);
				}
			}
		}
		return result;
	}


	public static String getString(int n) {
		String CHARS = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";
		StringBuilder uuid = new StringBuilder();
		Random rnd = new Random();
		while (uuid.length() < n) {
			int index = (int) (rnd.nextFloat() * CHARS.length());
			uuid.append(CHARS.charAt(index));
		}
		return uuid.toString();
	}


	public static boolean isValidMailbox(String str){
		EmailValidator validator = EmailValidator.getInstance();
		return validator.isValid(str);
	}

	public static String getBing(){
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String date = df.format(cal.getTime());
		return date;
	}

	public static long getToday(){
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat(Constants.ZERO_TIME_FORMAT);
		String date = df.format(cal.getTime());
		return Long.parseLong(date);
	}

	public static long getDate() {
		Calendar cal = Calendar.getInstance();
		long date = getDateFormatted(cal);
		return date;
	}

	public static long getYesterday(int day) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -day);
		long date = getDateFormatted(cal);
		return date;
	}

	private static long getDateFormatted(Calendar cal) {
		DateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
		String dateStr = df.format(cal.getTime());
		long date = Long.parseLong(dateStr);
		return date;
	}

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();
		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static Calendar clearTime(Calendar cal){
		cal.clear(Calendar.HOUR_OF_DAY);
		cal.clear(Calendar.AM_PM);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
		return cal;
	}

	public static String getPretty(Long date){
		String dateString = "";
		try {
			SimpleDateFormat parser = new SimpleDateFormat(Constants.DATE_FORMAT);
			Date d = parser.parse(Long.toString(date));

			SimpleDateFormat sdf2 = new SimpleDateFormat(Constants.DATE_PRETTY);
			dateString = sdf2.format(d);
		}catch(Exception ex){}
		return dateString;
	}

	private List<ProspectActivity> sort(List<ProspectActivity> activities){
		Comparator<ProspectActivity> comparator = new Comparator<ProspectActivity>() {
			@Override
			public int compare(ProspectActivity a1, ProspectActivity a2) {
				Long p1 = a1.getCompleteDate();
				Long p2 = a2.getCompleteDate();
				return p2.compareTo(p1);
			}
		};
		Collections.sort(activities, comparator);
		return activities;
	}

	public static boolean isTestEnv(Environment env){
		String[] profilesPre = env.getActiveProfiles();
		List<String> profiles = Arrays.asList(profilesPre);
		return profiles.contains(Constants.MOCK_ENVIRONMENT);
	}

	public static boolean isDevEnv(Environment env){
		String[] profilesPre = env.getActiveProfiles();
		List<String> profiles = Arrays.asList(profilesPre);
		return profiles.contains(Constants.DEV_ENVIRONMENT);
	}
}