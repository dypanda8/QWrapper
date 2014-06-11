import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import javax.xml.crypto.Data;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qunar.qfwrapper.bean.booking.BookingInfo;
import com.qunar.qfwrapper.bean.booking.BookingResult;
import com.qunar.qfwrapper.bean.search.FlightDetail;
import com.qunar.qfwrapper.bean.search.FlightSearchParam;
import com.qunar.qfwrapper.bean.search.FlightSegement;
import com.qunar.qfwrapper.bean.search.OneWayFlightInfo;
import com.qunar.qfwrapper.bean.search.ProcessResultInfo;
import com.qunar.qfwrapper.bean.search.RoundTripFlightInfo;
import com.qunar.qfwrapper.constants.Constants;
import com.qunar.qfwrapper.interfaces.QunarCrawler;
import com.qunar.qfwrapper.util.QFHttpClient;
import com.qunar.qfwrapper.util.QFPostMethod;

public class Wrapper_gjsairtu001 implements QunarCrawler {
	public static void main(String[] args) throws FileNotFoundException {
		FlightSearchParam searchParam = new FlightSearchParam();
		searchParam.setDep("TUN");
		searchParam.setArr("TIP");
		searchParam.setDepDate("2014-06-13");
		searchParam.setRetDate("2014-06-16");
		searchParam.setTimeOut("600000");
		searchParam.setToken("");

		System.out.println(searchParam);

		String code = new Wrapper_gjsairtu001().getHtml(searchParam);

		// System.out.println(code);

		// PrintWriter pw = new PrintWriter(new File("test.txt"));
		// pw.print(code);
		// pw.close();

		/*
		 * StringBuilder sb = new StringBuilder(); Scanner cin = new Scanner(new
		 * File("test.txt")); while (cin.hasNextLine()) {
		 * sb.append(cin.nextLine()); } String code = sb.toString();
		 */

		ProcessResultInfo result = new ProcessResultInfo();
		result = new Wrapper_gjsairtu001().process(code, searchParam);
		if (result.isRet() && result.getStatus().equals(Constants.SUCCESS)) {
			List<RoundTripFlightInfo> flightList = (List<RoundTripFlightInfo>) result
					.getData();
			for (RoundTripFlightInfo in : flightList) {
				System.out.println("************" + in.getInfo().toString());
				System.out.println("++++++++++++" + in.getDetail().toString());
			}
		} else {
			System.out.println(result.getStatus());
		}
	}

	@Override
	public BookingResult getBookingInfo(FlightSearchParam arg0) {
		String bookingUrlPre = "http://www.tunisair.com/site/publish/module/reservationFlex_frame.asp";

		BookingResult bookingResult = new BookingResult();
		BookingInfo bookingInfo = new BookingInfo();
		bookingInfo.setAction(bookingUrlPre);
		bookingInfo.setMethod("post");
		Map<String, String> map = new TreeMap<String, String>();

		String[] dateStrings = arg0.getDepDate().split("-");
		String fullDate = dateStrings[2] + "/" + dateStrings[1] + "/"
				+ dateStrings[0];

		String[] retDateStrings = arg0.getRetDate().split("-");
		String retFullDate = retDateStrings[2] + "/" + retDateStrings[1] + "/"
				+ retDateStrings[0];

		map.put("TRIP_TYPE", "R");
		map.put("B_LOCATION_1", arg0.getDep());
		map.put("E_LOCATION_1", arg0.getArr());
		map.put("DEBUT", fullDate);
		map.put("FIN", retFullDate);
		map.put("ADTPAX", "1");
		map.put("YTHPAX", "0");
		map.put("CHDPAX", "0");
		map.put("InfantPAX", "0");
		map.put("EMBEDDED_TRANSACTION", "FlexPricerAvailability");
		map.put("LANGUAGE", "GB");
		map.put("SITE", "BASXBASX");
		map.put("TRIP_FLOW", "YES");
		map.put("SEVEN_DAY_SEARCH", "TRUE");
		map.put("B_ANY_TIME_1", "TRUE");
		map.put("PRICING_TYPE", "O");
		map.put("DISPLAY_TYPE", "1");
		map.put("DATE_RANGE_VALUE_1", "4");
		map.put("DATE_RANGE_VALUE_2", "4");
		map.put("COMMERCIAL_FARE_FAMILY_1", "WWCFF");
		map.put("DATE_RANGE_QUALIFIER_1", "C");
		map.put("DATE_RANGE_QUALIFIER_2", "C");
		map.put("SO_SITE_FD_DISPLAY_MODE", "0");
		map.put("SO_SITE_ALLOW_SPECIAL_MEAL", "FALSE");
		map.put("SO_SITE_AVAIL_SERVICE_FEE", "TRUE");
		map.put("SO_SITE_ALLOW_DATA_TRANS_EXT", "TRUE");
		map.put("B_DATE_1", "");
		map.put("B_DATE_2", "");
		map.put("B_ANY_TIME_2", "TRUE");
		map.put("SEARCH_BY", "1");
		map.put("AIRLINE_1_1", "TU");
		map.put("EXTERNAL_ID", "FLEX-IBE-EN");
		map.put("DESTINATION_PAGE",
				"https://wftc2.e-travel.com/plnext/tunisair/Override.action");
		map.put("paiement", "BANQUE");
		map.put("x", "66");
		map.put("y", "10");
		map.put("lang", "en");
		bookingInfo.setInputs(map);
		bookingResult.setData(bookingInfo);
		bookingResult.setRet(true);
		return bookingResult;
	}

	private static String getHtmlF(FlightSearchParam arg0) {
		QFPostMethod postMethod = null;
		try {
			QFHttpClient httpClient = new QFHttpClient(arg0, false);

			String[] dateStrings = arg0.getDepDate().split("-");
			// year -- dateStrings[0]
			// month -- dateStrings[1]
			// day -- dateStrings[2]
			String[] retDateStrings = arg0.getRetDate().split("-");

			String postUrl = String
					.format("http://www.tunisair.com/site/publish/module/reservationFlex_frame.asp?TRIP_TYPE=R&B_LOCATION_1=%s&E_LOCATION_1=%s&DEBUT=%s%%2F%s%%2F%s&FIN=%s%%2F%s%%2F%s&ADTPAX=1&YTHPAX=0&CHDPAX=0&InfantPAX=0&EMBEDDED_TRANSACTION=FlexPricerAvailability&LANGUAGE=GB&SITE=BASXBASX&TRIP_FLOW=YES&SEVEN_DAY_SEARCH=TRUE&B_ANY_TIME_1=TRUE&PRICING_TYPE=O&DISPLAY_TYPE=1&DATE_RANGE_VALUE_1=4&DATE_RANGE_VALUE_2=4&COMMERCIAL_FARE_FAMILY_1=WWCFF&DATE_RANGE_QUALIFIER_1=C&DATE_RANGE_QUALIFIER_2=C&SO_SITE_FD_DISPLAY_MODE=0&SO_SITE_ALLOW_SPECIAL_MEAL=FALSE&SO_SITE_AVAIL_SERVICE_FEE=TRUE&SO_SITE_ALLOW_DATA_TRANS_EXT=TRUE&B_DATE_1=&B_DATE_2=&B_ANY_TIME_2=TRUE&SEARCH_BY=1&AIRLINE_1_1=TU&EXTERNAL_ID=FLEX-IBE-EN&DESTINATION_PAGE=https%%3A%%2F%%2Fwftc2.e-travel.com%%2Fplnext%%2Ftunisair%%2FOverride.action&paiement=BANQUE&x=18&y=8",
							arg0.getDep(), arg0.getArr(), dateStrings[2],
							dateStrings[1], dateStrings[0], retDateStrings[2],
							retDateStrings[1], retDateStrings[0]);

			postMethod = new QFPostMethod(postUrl);
			int status = httpClient.executeMethod(postMethod);

			return postMethod.getResponseBodyAsString();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != postMethod) {
				postMethod.releaseConnection();
			}
		}
		return "Exception";
	}

	private static String getHtml0(FlightSearchParam arg0, String OFFICE_ID) {
		QFPostMethod postMethod = null;
		try {
			QFHttpClient httpClient = new QFHttpClient(arg0, false);

			String[] dateStrings = arg0.getDepDate().split("-");
			// year -- dateStrings[0]
			// month -- dateStrings[1]
			// day -- dateStrings[2]
			String[] retDateStrings = arg0.getRetDate().split("-");

			String dateStringFull = dateStrings[0] + dateStrings[1]
					+ dateStrings[2] + "0000";
			String retDateStringFull = retDateStrings[0] + retDateStrings[1]
					+ retDateStrings[2] + "0000";

			String postUrl = String
					.format("http://wftc2.e-travel.com/plnext/tunisair/Override.action?B_LOCATION_1=%s&E_LOCATION_1=%s&TRIP_TYPE=R&B_Day=%s&B_Month=%s&B_YEAR=%s&B_TIME=0000&dcd1=&E_DAY=%s&E_MONTH=%s&E_YEAR=%s&E_TIME=0000&dcd2=&EMBEDDED_TRANSACTION=FlexPricerAvailability&LANGUAGE=GB&SITE=BASXBASX&TRIP_FLOW=YES&B_ANY_TIME_1=TRUE&B_DATE_1=201407280000&B_DATE_2=201407310000&B_ANY_TIME_2=TRUE&CORPORATE_CODE=&CORPORATE_TYPE=&AIRLINE_1_1=TU&AIRLINE_2_1=&EXTERNAL_ID=FLEX-IBE-EN&SESSION_ID=&SO_GL=%%3C%%3Fxml+version%%3D%%221.0%%22+encoding%%3D%%22iso-8859-1%%22%%3F%%3E%%3CSO_GL%%3E%%3CGLOBAL_LIST+mode%%3D%%22partial%%22%%3E%%3CNAME%%3ESITE_SITE_FARE_COMMANDS_AND_OPTIONS%%3C%%2FNAME%%3E%%3CLIST_ELEMENT%%3E%%3CCODE%%3E104%%3C%%2FCODE%%3E%%3CLIST_VALUE%%3E0%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E2%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E4%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E0%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3C%%2FLIST_ELEMENT%%3E%%3C%%2FGLOBAL_LIST%%3E%%3C%%2FSO_GL%%3E&SEARCH_BY=1&DESTINATION_PAGE=http%%3A%%2F%%2Fwftc2.e-travel.com%%2Fplnext%%2Ftunisair%%2FOverride.action&ADTPAX=1&YTHPAX=0&CHDPAX=0&InfantPAX=0&PRICING_TYPE=O&DISPLAY_TYPE=1&DATE_RANGE_VALUE_1=4&DATE_RANGE_VALUE_2=4&COMMERCIAL_FARE_FAMILY_1=WWCFF&DATE_RANGE_QUALIFIER_1=C&DATE_RANGE_QUALIFIER_2=C&SO_SITE_FD_DISPLAY_MODE=0&SO_SITE_ALLOW_SPECIAL_MEAL=FALSE&SO_SITE_AVAIL_SERVICE_FEE=TRUE&SO_SITE_CURRENCY_FORMAT_JAVA=0.000&SO_SITE_ALLOW_DATA_TRANS_EXT=True&DIRECT_NON_STOP=TRUE&SO_SITE_OFFICE_ID="
							+ OFFICE_ID
							+ "&SO_SITE_MOP_EXT=TRUE&SO_SITE_MOP_CALL_ME=FALSE&SO_SITE_DATA_TRANSFER=FALSE&SO_SITE_BOOL_ISSUE_ETKT=TRUE&SO_SITE_USER_CURRENCY_CODE=TND&SO_SITE_EXT_PSPURL=https%%3A%%2F%%2Fwww.smt-sps.com.tn%%2FClickToPay%%2Findex.aspx&SO_SITE_MINIMAL_TIME=H6&TRAVELLER_TYPE_1=ADT",
							arg0.getDep(), arg0.getArr(), dateStrings[2],
							dateStrings[1], dateStrings[0], retDateStrings[2],
							retDateStrings[1], retDateStrings[0],
							dateStringFull, retDateStringFull);

			postMethod = new QFPostMethod(postUrl);
			int status = httpClient.executeMethod(postMethod);

			return postMethod.getResponseBodyAsString();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != postMethod) {
				postMethod.releaseConnection();
			}
		}
		return "Exception";
	}

	private static String getPostUrl(String code) {
		int startPos = code.indexOf("FlexPricerAvailabilityDispatcherPui");
		if (startPos < 0) {
			return null;
		}
		int endPos = code.indexOf("\">", startPos);
		return code.substring(startPos, endPos);
	}

	private static final NameValuePair POST_FORMS_YTHPAX = new NameValuePair(
			"YTHPAX", "0");
	private static final NameValuePair POST_FORMS_DIRECT_NON_STOP = new NameValuePair(
			"DIRECT_NON_STOP", "TRUE");
	private static final NameValuePair POST_FORMS_DATE_RANGE_VALUE_2 = new NameValuePair(
			"DATE_RANGE_VALUE_2", "0");
	private static final NameValuePair POST_FORMS_DATE_RANGE_VALUE_1 = new NameValuePair(
			"DATE_RANGE_VALUE_1", "0");
	private static final NameValuePair POST_FORMS_CORPORATE_CODE = new NameValuePair(
			"CORPORATE_CODE", "");
	private static final NameValuePair POST_FORMS_TRAVELLER_TYPE_1 = new NameValuePair(
			"TRAVELLER_TYPE_1", "ADT");
	private static final NameValuePair POST_FORMS_SEARCH_BY = new NameValuePair(
			"SEARCH_BY", "1");
	private static final NameValuePair POST_FORMS_B_TIME = new NameValuePair(
			"B_TIME", "0000");
	private static final NameValuePair POST_FORMS_DATE_RANGE_QUALIFIER_1 = new NameValuePair(
			"DATE_RANGE_QUALIFIER_1", "C");
	private static final NameValuePair POST_FORMS_DATE_RANGE_QUALIFIER_2 = new NameValuePair(
			"DATE_RANGE_QUALIFIER_2", "C");
	private static final NameValuePair POST_FORMS_DESTINATION_PAGE = new NameValuePair(
			"DESTINATION_PAGE",
			"http://wftc2.e-travel.com/plnext/tunisair/Override.action");
	private static final NameValuePair POST_FORMS_B_ANY_TIME_1 = new NameValuePair(
			"B_ANY_TIME_1", "TRUE");
	private static final NameValuePair POST_FORMS_E_TIME = new NameValuePair(
			"E_TIME", "0000");
	private static final NameValuePair POST_FORMS_B_ANY_TIME_2 = new NameValuePair(
			"B_ANY_TIME_2", "TRUE");
	private static final NameValuePair POST_FORMS_AIRLINE_1_1 = new NameValuePair(
			"AIRLINE_1_1", "TU");
	private static final NameValuePair POST_FORMS_AIRLINE_2_1 = new NameValuePair(
			"AIRLINE_2_1", "");
	private static final NameValuePair POST_FORMS_CHDPAX = new NameValuePair(
			"CHDPAX", "0");
	private static final NameValuePair POST_FORMS_SITE = new NameValuePair(
			"SITE", "BASXBASX");
	private static final NameValuePair POST_FORMS_DISPLAY_TYPE = new NameValuePair(
			"DISPLAY_TYPE", "1");
	private static final NameValuePair POST_FORMS_TRIP_FLOW = new NameValuePair(
			"TRIP_FLOW", "YES");
	private static final NameValuePair POST_FORMS_CORPORATE_TYPE = new NameValuePair(
			"CORPORATE_TYPE", "");
	private static final NameValuePair POST_FORMS_TRIP_TYPE = new NameValuePair(
			"TRIP_TYPE", "R");
	private static final NameValuePair POST_FORMS_EXTERNAL_ID = new NameValuePair(
			"EXTERNAL_ID", "FLEX-IBE-EN");
	private static final NameValuePair POST_FORMS_COMMERCIAL_FARE_FAMILY_1 = new NameValuePair(
			"COMMERCIAL_FARE_FAMILY_1", "WWCFF");
	private static final NameValuePair POST_FORMS_PRICING_TYPE = new NameValuePair(
			"PRICING_TYPE", "O");
	private static final NameValuePair POST_FORMS_LANGUAGE = new NameValuePair(
			"LANGUAGE", "GB");
	private static final NameValuePair POST_FORMS_ADTPAX = new NameValuePair(
			"ADTPAX", "1");
	private static final NameValuePair POST_FORMS_InfantPAX = new NameValuePair(
			"InfantPAX", "0");
	private static final NameValuePair POST_FORMS_dcd1 = new NameValuePair(
			"dcd1", "");
	private static final NameValuePair POST_FORMS_dcd2 = new NameValuePair(
			"dcd2", "");
	private static final NameValuePair POST_FORMS_PLTG_IS_UPSELL = new NameValuePair(
			"PLTG_IS_UPSELL", "true");
	private static final NameValuePair POST_FORMS_PAGE_TICKET = new NameValuePair(
			"PAGE_TICKET", "0");

	@Override
	public String getHtml(FlightSearchParam arg0) {
		String OFFICE_ID = null;
		try {
			// Get office_id ... orz
			String frameCode = getHtmlF(arg0);
			OFFICE_ID = StringUtils.substringBetween(frameCode,
					"SITE_OFFICE_ID\" value=\"", "\"/>");
			// System.out.println(OFFICE_ID);
		} catch (Exception e) {
			e.printStackTrace();
			return "Exception";
		}

		String postUrl = null;
		String searchResultCode = null;
		try {
			// Get first page for jsessionid ... orz
			searchResultCode = getHtml0(arg0, OFFICE_ID);
			postUrl = getPostUrl(searchResultCode);
			// System.out.print(postUrl);
		} catch (Exception e) {
			e.printStackTrace();
			return "Exception";
		}

		// judge No Result
		if (null == postUrl
				|| searchResultCode
						.contains("All flights for this date may be sold out.")) {
			return "NoResult";
		}

		QFPostMethod postMethod = new QFPostMethod(
				"http://wftc2.e-travel.com/plnext/tunisair/" + postUrl);
		try {
			QFHttpClient httpClient = new QFHttpClient(arg0, false);

			String[] dateStrings = arg0.getDepDate().split("-");
			// year -- dateStrings[0]
			// month -- dateStrings[1]
			// day -- dateStrings[2]
			String[] retDateStrings = arg0.getRetDate().split("-");

			String dateStringFull = dateStrings[0] + dateStrings[1]
					+ dateStrings[2] + "0000";
			String retDateStringFull = retDateStrings[0] + retDateStrings[1]
					+ retDateStrings[2] + "0000";

			NameValuePair[] forms = { POST_FORMS_YTHPAX,
					new NameValuePair("B_YEAR", dateStrings[0]),
					POST_FORMS_DIRECT_NON_STOP,
					new NameValuePair("B_LOCATION_1", arg0.getDep()),
					POST_FORMS_DATE_RANGE_VALUE_2,
					POST_FORMS_DATE_RANGE_VALUE_1, POST_FORMS_CORPORATE_CODE,
					new NameValuePair("E_LOCATION_1", arg0.getArr()),
					POST_FORMS_TRAVELLER_TYPE_1, POST_FORMS_SEARCH_BY,
					new NameValuePair("E_MONTH", retDateStrings[1]),
					POST_FORMS_B_TIME, POST_FORMS_DATE_RANGE_QUALIFIER_1,
					POST_FORMS_DATE_RANGE_QUALIFIER_2,
					new NameValuePair("B_Day", dateStrings[2]),
					new NameValuePair("B_DATE_1", dateStringFull),
					POST_FORMS_DESTINATION_PAGE,
					new NameValuePair("B_DATE_2", retDateStringFull),
					POST_FORMS_B_ANY_TIME_1, POST_FORMS_E_TIME,
					new NameValuePair("SESSION_ID", ""),
					POST_FORMS_B_ANY_TIME_2, POST_FORMS_AIRLINE_1_1,
					POST_FORMS_AIRLINE_2_1, POST_FORMS_CHDPAX, POST_FORMS_SITE,
					POST_FORMS_DISPLAY_TYPE,
					new NameValuePair("E_YEAR", retDateStrings[0]),
					POST_FORMS_TRIP_FLOW, POST_FORMS_CORPORATE_TYPE,
					POST_FORMS_TRIP_TYPE, POST_FORMS_EXTERNAL_ID,
					new NameValuePair("OFFICE_ID", OFFICE_ID),
					new NameValuePair("B_Month", dateStrings[1]),
					new NameValuePair("E_DAY", retDateStrings[2]),
					POST_FORMS_COMMERCIAL_FARE_FAMILY_1,
					POST_FORMS_PRICING_TYPE, POST_FORMS_LANGUAGE,
					POST_FORMS_ADTPAX, POST_FORMS_InfantPAX, POST_FORMS_dcd1,
					POST_FORMS_dcd2, POST_FORMS_PLTG_IS_UPSELL,
					POST_FORMS_PAGE_TICKET };

			postMethod.setRequestBody(forms);
			postMethod
					.setRequestHeader("Referer",
							"http://wftc2.e-travel.com/plnext/tunisair/Override.action");
			postMethod.getParams().setContentCharset("UTF-8");

			// for (NameValuePair iter : forms) {
			// System.out.println(iter.getName() + "\t" + iter.getValue());
			// }

			int status = httpClient.executeMethod(postMethod);
			// System.out.println(postMethod.getResponseBodyAsString());

			return postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != postMethod) {
				postMethod.releaseConnection();
			}
		}
		return "Exception";
	}

	@Override
	public ProcessResultInfo process(String html, FlightSearchParam param) {
		ProcessResultInfo result = new ProcessResultInfo();
		if ("Exception".equals(html)) {
			result.setRet(false);
			result.setStatus(Constants.CONNECTION_FAIL);
			return result;
		}
		if ("NoResult".equals(html)) {
			result.setRet(true);
			result.setStatus(Constants.NO_RESULT);
			return result;
		}

		String jsonStr = StringUtils.substringBetween(html,
				"var generatedJSon = new String('", "');");
		// System.out.println(jsonStr);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Map<String, FlightSegement> flightSegInfo = new HashMap<String, FlightSegement>();

		try {
			JSONObject rootJson = JSON.parseObject(jsonStr);
			JSONObject listTabJson = rootJson.getJSONObject("list_tab");
			JSONArray listProposedBoundJson = listTabJson
					.getJSONArray("list_proposed_bound");
			for (int i = 0; i < listProposedBoundJson.size(); i++) {
				JSONObject listProposedBoundObj = listProposedBoundJson
						.getJSONObject(i);
				JSONArray listFlightJson = listProposedBoundObj
						.getJSONArray("list_flight");
				for (int j = 0; j < listFlightJson.size(); j++) {
					JSONObject flightJson = listFlightJson.getJSONObject(j);
					JSONArray listSegmentJson = flightJson
							.getJSONArray("list_segment");
					for (int k = 0; k < listSegmentJson.size(); k++) {
						JSONObject segJson = listSegmentJson.getJSONObject(k);

						String airLineCode = segJson.getJSONObject("airline")
								.getString("code");
						String flightNumber = segJson
								.getString("flight_number");

						// System.out.println(airLineCode+flightNumber);

						FlightSegement seg = new FlightSegement();
						seg.setFlightno(airLineCode + flightNumber);
						seg.setDepDate(dateFormater(segJson
								.getString("b_date_date")));
						seg.setDepairport(segJson.getJSONObject("b_location")
								.getString("location_code"));
						seg.setDeptime(segJson
								.getString("b_date_formatted_time"));

						seg.setArrDate(dateFormater(segJson
								.getString("e_date_date")));
						seg.setArrairport(segJson.getJSONObject("e_location")
								.getString("location_code"));
						seg.setArrtime(segJson
								.getString("e_date_formatted_time"));

						flightSegInfo.put(flightNumber, seg);
					}
				}
			}

			JSONArray listRecommendationJson = listTabJson
					.getJSONArray("list_recommendation");
			int cnt = 0;

			Map<String, Double> lowestPrice = new HashMap<String, Double>();
			Map<String, Double> amountWithoutTax = new HashMap<String, Double>();

			Map<String, Double> flightAPrice = new HashMap<String, Double>();
			Map<String, Double> flightBPrice = new HashMap<String, Double>();

			Map<String, Double> flightAWithoutPrice = new HashMap<String, Double>();
			Map<String, Double> flightBWithoutPrice = new HashMap<String, Double>();

			Map<String, Double> flightATax = new HashMap<String, Double>();
			Map<String, Double> flightBTax = new HashMap<String, Double>();

			Map<String, String> flightACur = new HashMap<String, String>();
			Map<String, String> flightBCur = new HashMap<String, String>();

			for (int i = 0; i < listRecommendationJson.size(); i++) {
				JSONObject listRecommendationObj = listRecommendationJson
						.getJSONObject(i);
				JSONArray listBoundJson = listRecommendationObj
						.getJSONArray("list_bound");

				Double priceNow = listRecommendationObj.getDouble("price");
				// System.out.println(priceNow);

				JSONArray listTripPrice = listRecommendationObj
						.getJSONArray("list_trip_price");
				JSONObject listTripPriceObj = listTripPrice.getJSONObject(0);

				Double priceWithOutTax = listTripPriceObj
						.getDouble("amount_without_tax_float");

				JSONArray listBoundPriceJson = listTripPriceObj
						.getJSONArray("list_bound_price");
				Double flightAPriceWithoutNow = listBoundPriceJson
						.getJSONObject(0).getDouble("amount_without_tax_float");
				Double flightBPriceWithoutNow = listBoundPriceJson
						.getJSONObject(1).getDouble("amount_without_tax_float");

				String currencyANow = listBoundPriceJson.getJSONObject(0)
						.getJSONObject("currency").getString("code");
				String currencyBNow = listBoundPriceJson.getJSONObject(1)
						.getJSONObject("currency").getString("code");

				JSONArray listPriceJson = listRecommendationObj
						.getJSONArray("list_price");
				Double flightAPriceNow = listPriceJson.getJSONObject(0)
						.getDouble("price");
				Double flightBPriceNow = listPriceJson.getJSONObject(1)
						.getDouble("price");

				Double flightATaxNow = flightAPriceNow - flightAPriceWithoutNow;
				Double flightBTaxNow = flightBPriceNow - flightBPriceWithoutNow;

				JSONObject listBoundObj0 = listBoundJson.getJSONObject(0);
				JSONArray listFlightJson0 = listBoundObj0
						.getJSONArray("list_flight");

				JSONObject listBoundObj1 = listBoundJson.getJSONObject(1);
				JSONArray listFlightJson1 = listBoundObj1
						.getJSONArray("list_flight");

				for (int j = 0; j < listFlightJson0.size(); j++) {
					JSONObject flightJson0 = listFlightJson0.getJSONObject(j);
					for (int k = 0; k < listFlightJson1.size(); k++) {
						JSONObject flightJson1 = listFlightJson1
								.getJSONObject(k);
						String combineName = flightJson0.getJSONObject(
								"lsa_debug_info").getString(
								"first_flight_number")
								+ ","
								+ flightJson1.getJSONObject("lsa_debug_info")
										.getString("first_flight_number");

						if (lowestPrice.containsKey(combineName)) {
							Double lPrice = lowestPrice.get(combineName);
							if (lPrice > priceNow) {
								lowestPrice.put(combineName, priceNow);
								amountWithoutTax.put(combineName,
										priceWithOutTax);
								flightAPrice.put(combineName, flightAPriceNow);
								flightBPrice.put(combineName, flightBPriceNow);
								flightAWithoutPrice.put(combineName,
										flightAPriceWithoutNow);
								flightBWithoutPrice.put(combineName,
										flightBPriceWithoutNow);
								flightATax.put(combineName, flightATaxNow);
								flightBTax.put(combineName, flightBTaxNow);
								flightACur.put(combineName, currencyANow);
								flightBCur.put(combineName, currencyBNow);
							}
						} else {
							lowestPrice.put(combineName, priceNow);
							amountWithoutTax.put(combineName, priceWithOutTax);
							flightAPrice.put(combineName, flightAPriceNow);
							flightBPrice.put(combineName, flightBPriceNow);
							flightAWithoutPrice.put(combineName,
									flightAPriceWithoutNow);
							flightBWithoutPrice.put(combineName,
									flightBPriceWithoutNow);
							flightATax.put(combineName, flightATaxNow);
							flightBTax.put(combineName, flightBTaxNow);
							flightACur.put(combineName, currencyANow);
							flightBCur.put(combineName, currencyBNow);
						}
					}
				}
			}

			List<RoundTripFlightInfo> flightList = new ArrayList<RoundTripFlightInfo>();

			for (Entry<String, Double> iter : lowestPrice.entrySet()) {
				String flightAId = iter.getKey().split(",")[0];
				String flightBId = iter.getKey().split(",")[1];

				RoundTripFlightInfo flightInfo = new RoundTripFlightInfo();

				FlightSegement segA = flightSegInfo.get(flightAId);
				FlightSegement segB = flightSegInfo.get(flightBId);

				List<FlightSegement> segs = new ArrayList<FlightSegement>();
				segs.add(segA);
				List<FlightSegement> segsRet = new ArrayList<FlightSegement>();
				segsRet.add(segB);

				FlightDetail flightDetail = new FlightDetail();
				flightDetail.setArrcity(param.getArr());
				flightDetail.setDepcity(param.getDep());

				Date tmpDate = dateFormat.parse(segA.getDepDate() + " "
						+ segA.getDeptime());

				flightDetail.setDepdate(tmpDate);
				List<String> flightNo = new ArrayList<String>();
				flightNo.add(segA.getFlightno());
				flightDetail.setFlightno(flightNo);
				flightDetail.setMonetaryunit(flightACur.get(iter.getKey()));
				flightDetail.setPrice(amountWithoutTax.get(iter.getKey()));
				flightDetail.setTax(iter.getValue()
						- amountWithoutTax.get(iter.getKey()));
				flightDetail.setWrapperid(param.getWrapperid());

				flightInfo.setDetail(flightDetail);
				flightInfo.setInfo(segs);

				flightInfo.setOutboundPrice(flightAWithoutPrice.get(iter
						.getKey()));

				tmpDate = dateFormat.parse(segB.getDepDate() + " "
						+ segB.getDeptime());

				flightInfo.setRetdepdate(tmpDate);

				List<String> retFlightNo = new ArrayList<String>();
				retFlightNo.add(segB.getFlightno());
				flightInfo.setRetflightno(retFlightNo);

				flightInfo.setRetinfo(segsRet);
				flightInfo.setReturnedPrice(flightBWithoutPrice.get(iter
						.getKey()));

				flightList.add(flightInfo);
			}

			result.setRet(true);
			result.setStatus(Constants.SUCCESS);
			result.setData(flightList);
		} catch (Exception e) {
			e.printStackTrace();
			result.setRet(false);
			result.setStatus(Constants.PARSING_FAIL);
		}
		return result;
	}

	private static String dateFormater(String date) {
		return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
				+ date.substring(6, 8);
	}
}
