import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import com.qunar.qfwrapper.constants.Constants;
import com.qunar.qfwrapper.interfaces.QunarCrawler;
import com.qunar.qfwrapper.util.QFHttpClient;
import com.qunar.qfwrapper.util.QFPostMethod;

public class Wrapper_gjdairtu001 implements QunarCrawler {
	public static void main(String[] args) throws FileNotFoundException {
		FlightSearchParam searchParam = new FlightSearchParam();
		searchParam.setDep("TIP");
		searchParam.setArr("CAI");
		searchParam.setDepDate("2014-06-12");
		searchParam.setTimeOut("600000");
		searchParam.setToken("");

		System.out.println(searchParam);

		String code = new Wrapper_gjdairtu001().getHtml(searchParam);

		// PrintWriter pw = new PrintWriter(new File("test.txt"));
		// pw.print(code);
		// pw.close();

		// String code = "";
		// Scanner cin = new Scanner(new File("test.txt"));
		// while (cin.hasNextLine()) {
		// String line = cin.nextLine();
		// code = code + line;
		// }

		ProcessResultInfo result = new ProcessResultInfo();
		result = new Wrapper_gjdairtu001().process(code, searchParam);
		if (result.isRet() && result.getStatus().equals(Constants.SUCCESS)) {
			List<OneWayFlightInfo> flightList = (List<OneWayFlightInfo>) result
					.getData();
			for (OneWayFlightInfo in : flightList) {
				System.out.println("************" + in.getInfo().toString());
				System.out.println("++++++++++++" + in.getDetail().toString());
			}
		} else {
			System.out.println(result.getStatus());
		}
	}

	@Override
	public BookingResult getBookingInfo(FlightSearchParam arg0) {
		String bookingUrlPre = "http://wftc2.e-travel.com/plnext/tunisair/Fare.action;jsessionid=wV2Kip6XrM1faV8N80KlTioDtuFO4agebs-LU9rEdW-7Sa_nLWkw!193673219!-1819211208";
		
		
		
		BookingResult bookingResult = new BookingResult();
		BookingInfo bookingInfo = new BookingInfo();
		//bookingInfo.setAction(action);
		
		return null;
	}

	private static String getHtmlF(FlightSearchParam arg0) {
		QFPostMethod postMethod = null;
		try {
			QFHttpClient httpClient = new QFHttpClient(arg0, false);

			String[] dateStrings = arg0.getDepDate().split("-");
			// year -- dateStrings[0]
			// month -- dateStrings[1]
			// day -- dateStrings[2]
			String dateStringFull = dateStrings[0] + dateStrings[1]
					+ dateStrings[2] + "0000";

			String postUrl = String
					.format("http://www.tunisair.com/site/publish/module/reservationFlex_frame.asp?TRIP_TYPE=O&B_LOCATION_1=%s&E_LOCATION_1=%s&DEBUT=%s%%2F%s%%2F%s&FIN=%s%%2F%s%%2F%s&ADTPAX=1&YTHPAX=0&CHDPAX=0&InfantPAX=0&EMBEDDED_TRANSACTION=FlexPricerAvailability&LANGUAGE=GB&SITE=BASXBASX&TRIP_FLOW=YES&SEVEN_DAY_SEARCH=TRUE&B_ANY_TIME_1=TRUE&PRICING_TYPE=O&DISPLAY_TYPE=1&DATE_RANGE_VALUE_1=4&DATE_RANGE_VALUE_2=4&COMMERCIAL_FARE_FAMILY_1=WWCFF&DATE_RANGE_QUALIFIER_1=C&DATE_RANGE_QUALIFIER_2=C&SO_SITE_FD_DISPLAY_MODE=0&SO_SITE_ALLOW_SPECIAL_MEAL=FALSE&SO_SITE_AVAIL_SERVICE_FEE=TRUE&SO_SITE_ALLOW_DATA_TRANS_EXT=TRUE&B_DATE_1=&B_DATE_2=&B_ANY_TIME_2=TRUE&SEARCH_BY=1&AIRLINE_1_1=TU&EXTERNAL_ID=FLEX-IBE-EN&DESTINATION_PAGE=https%%3A%%2F%%2Fwftc2.e-travel.com%%2Fplnext%%2Ftunisair%%2FOverride.action&paiement=BANQUE&x=34&y=13",
							arg0.getDep(), arg0.getArr(), dateStrings[2],
							dateStrings[1], dateStrings[0], dateStrings[2],
							dateStrings[1], dateStrings[0]);

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
			String dateStringFull = dateStrings[0] + dateStrings[1]
					+ dateStrings[2] + "0000";

			String postUrl = String
					.format("http://wftc2.e-travel.com/plnext/tunisair/Override.action?B_LOCATION_1=%s&E_LOCATION_1=%s&TRIP_TYPE=O&B_Day=%s&B_Month=%s&B_YEAR=%s&B_TIME=0000&dcd1=&E_DAY=%s&E_MONTH=%s&E_YEAR=%s&E_TIME=0000&dcd2=&EMBEDDED_TRANSACTION=FlexPricerAvailability&LANGUAGE=GB&SITE=BASXBASX&TRIP_FLOW=YES&B_ANY_TIME_1=TRUE&B_DATE_1=%s&B_DATE_2=%s&B_ANY_TIME_2=TRUE&CORPORATE_CODE=&CORPORATE_TYPE=&AIRLINE_1_1=TU&AIRLINE_2_1=&EXTERNAL_ID=FLEX-IBE-EN&SESSION_ID=&SO_GL=%%3C%%3Fxml+version%%3D%%221.0%%22+encoding%%3D%%22iso-8859-1%%22%%3F%%3E%%3CSO_GL%%3E%%3CGLOBAL_LIST+mode%%3D%%22partial%%22%%3E%%3CNAME%%3ESITE_SITE_FARE_COMMANDS_AND_OPTIONS%%3C%%2FNAME%%3E%%3CLIST_ELEMENT%%3E%%3CCODE%%3E104%%3C%%2FCODE%%3E%%3CLIST_VALUE%%3E0%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E2%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E4%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E0%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3CLIST_VALUE%%3E%%3C%%2FLIST_VALUE%%3E%%3C%%2FLIST_ELEMENT%%3E%%3C%%2FGLOBAL_LIST%%3E%%3C%%2FSO_GL%%3E&SEARCH_BY=1&DESTINATION_PAGE=http%%3A%%2F%%2Fwftc2.e-travel.com%%2Fplnext%%2Ftunisair%%2FOverride.action&ADTPAX=1&YTHPAX=0&CHDPAX=0&InfantPAX=0&PRICING_TYPE=O&DISPLAY_TYPE=1&DATE_RANGE_VALUE_1=4&DATE_RANGE_VALUE_2=4&COMMERCIAL_FARE_FAMILY_1=WWCFF&DATE_RANGE_QUALIFIER_1=C&DATE_RANGE_QUALIFIER_2=C&SO_SITE_FD_DISPLAY_MODE=0&SO_SITE_ALLOW_SPECIAL_MEAL=FALSE&SO_SITE_AVAIL_SERVICE_FEE=TRUE&SO_SITE_CURRENCY_FORMAT_JAVA=0.000&SO_SITE_ALLOW_DATA_TRANS_EXT=True&DIRECT_NON_STOP=TRUE&SO_SITE_OFFICE_ID="
							+ OFFICE_ID
							+ "&SO_SITE_MOP_EXT=TRUE&SO_SITE_MOP_CALL_ME=FALSE&SO_SITE_DATA_TRANSFER=FALSE&SO_SITE_BOOL_ISSUE_ETKT=TRUE&SO_SITE_USER_CURRENCY_CODE=TND&SO_SITE_EXT_PSPURL=https%%3A%%2F%%2Fwww.smt-sps.com.tn%%2FClickToPay%%2Findex.aspx&SO_SITE_MINIMAL_TIME=H6&TRAVELLER_TYPE_1=ADT",
							arg0.getDep(), arg0.getArr(), dateStrings[2],
							dateStrings[1], dateStrings[0], dateStrings[2],
							dateStrings[1], dateStrings[0], dateStringFull,
							dateStringFull);

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
			"TRIP_TYPE", "O");
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
			System.out.println(OFFICE_ID);
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
			String dateStringFull = dateStrings[0] + dateStrings[1]
					+ dateStrings[2] + "0000";

			NameValuePair[] forms = { POST_FORMS_YTHPAX,
					new NameValuePair("B_YEAR", dateStrings[0]),
					POST_FORMS_DIRECT_NON_STOP,
					new NameValuePair("B_LOCATION_1", arg0.getDep()),
					POST_FORMS_DATE_RANGE_VALUE_2,
					POST_FORMS_DATE_RANGE_VALUE_1, POST_FORMS_CORPORATE_CODE,
					new NameValuePair("E_LOCATION_1", arg0.getArr()),
					POST_FORMS_TRAVELLER_TYPE_1, POST_FORMS_SEARCH_BY,
					new NameValuePair("E_MONTH", dateStrings[1]),
					POST_FORMS_B_TIME, POST_FORMS_DATE_RANGE_QUALIFIER_1,
					POST_FORMS_DATE_RANGE_QUALIFIER_2,
					new NameValuePair("B_Day", dateStrings[2]),
					new NameValuePair("B_DATE_1", dateStringFull),
					POST_FORMS_DESTINATION_PAGE,
					new NameValuePair("B_DATE_2", dateStringFull),
					POST_FORMS_B_ANY_TIME_1, POST_FORMS_E_TIME,
					new NameValuePair("SESSION_ID", ""),
					POST_FORMS_B_ANY_TIME_2, POST_FORMS_AIRLINE_1_1,
					POST_FORMS_AIRLINE_2_1, POST_FORMS_CHDPAX, POST_FORMS_SITE,
					POST_FORMS_DISPLAY_TYPE,
					new NameValuePair("E_YEAR", dateStrings[0]),
					POST_FORMS_TRIP_FLOW, POST_FORMS_CORPORATE_TYPE,
					POST_FORMS_TRIP_TYPE, POST_FORMS_EXTERNAL_ID,
					new NameValuePair("OFFICE_ID", OFFICE_ID),
					new NameValuePair("B_Month", dateStrings[1]),
					new NameValuePair("E_DAY", dateStrings[2]),
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

		try {
			JSONArray listRecommendationJson = JSON.parseObject(jsonStr)
					.getJSONObject("list_tab")
					.getJSONArray("list_recommendation");

			Map<String, Double> flightLowestCost = new TreeMap<String, Double>();
			Map<String, String> currency = new TreeMap<String, String>();
			Map<String, Double> flightTaxCost = new TreeMap<String, Double>();
			Map<String, Double> flightPriceCost = new TreeMap<String, Double>();

			for (int i = 0; i < listRecommendationJson.size(); i++) {
				JSONObject fareTypeJson = listRecommendationJson
						.getJSONObject(i);
				JSONObject priceJson = fareTypeJson.getJSONArray(
						"list_trip_price").getJSONObject(0);

				String monetaryunit = priceJson.getJSONObject("currency")
						.getString("code");

				Double totPrice = fareTypeJson.getDouble("price");
				Double priceWithoutTaxDouble = priceJson
						.getDouble("amount_without_tax_float");
				Double taxDouble = totPrice - priceWithoutTaxDouble;

				JSONArray avaArray = fareTypeJson.getJSONArray("list_bound")
						.getJSONObject(0).getJSONArray("list_flight");
				for (int j = 0; j < avaArray.size(); j++) {
					JSONObject flight = avaArray.getJSONObject(j);
					String flightId = flight.getString("flight_id");
					// System.out.println(flightId);
					if (flightLowestCost.containsKey(flightId)) {
						Double cost = flightLowestCost.get(flightId);
						if (cost > totPrice) {
							flightLowestCost.put(flightId, totPrice);
							currency.put(flightId, monetaryunit);
							flightTaxCost.put(flightId, taxDouble);
							flightPriceCost
									.put(flightId, priceWithoutTaxDouble);
						}
					} else {
						flightLowestCost.put(flightId, totPrice);
						currency.put(flightId, monetaryunit);
						flightTaxCost.put(flightId, taxDouble);
						flightPriceCost.put(flightId, priceWithoutTaxDouble);
					}
				}
			}

			List<OneWayFlightInfo> flightList = new ArrayList<OneWayFlightInfo>();
			JSONArray listFlightJsonArray = JSON.parseObject(jsonStr)
					.getJSONObject("list_tab")
					.getJSONArray("list_proposed_bound").getJSONObject(0)
					.getJSONArray("list_flight");

			for (int i = 0; i < listFlightJsonArray.size(); i++) {
				OneWayFlightInfo flight = new OneWayFlightInfo();
				List<FlightSegement> segs = new ArrayList<FlightSegement>();
				FlightDetail flightDetail = new FlightDetail();

				List<String> flightNoList = new ArrayList<String>();

				JSONObject flightJson = listFlightJsonArray.getJSONObject(i);
				JSONArray flightSeg = flightJson.getJSONArray("list_segment");

				Date depDate = null;

				for (int j = 0; j < flightSeg.size(); j++) {
					JSONObject segJson = flightSeg.getJSONObject(j);

					String airLineCode = segJson.getJSONObject("airline")
							.getString("code");
					String flightNumber = segJson.getString("flight_number");
					flightNoList.add(airLineCode + flightNumber);

					FlightSegement seg = new FlightSegement();
					seg.setFlightno(airLineCode + flightNumber);
					seg.setDepDate(dateFormater(segJson
							.getString("b_date_date")));
					seg.setDepairport(segJson.getJSONObject("b_location")
							.getString("location_code"));
					seg.setDeptime(segJson.getString("b_date_formatted_time"));

					seg.setArrDate(dateFormater(segJson
							.getString("e_date_date")));
					seg.setArrairport(segJson.getJSONObject("e_location")
							.getString("location_code"));
					seg.setArrtime(segJson.getString("e_date_formatted_time"));

					segs.add(seg);
					// System.out.println(seg);

					Date tmpDate = dateFormat.parse(seg.getDepDate() + " "
							+ seg.getDeptime());
					if (depDate == null
							|| tmpDate.getTime() < depDate.getTime()) {
						depDate = tmpDate;
					}
				}

				flightDetail.setDepdate(depDate);
				flightDetail.setDepcity(param.getDep());
				flightDetail.setArrcity(param.getArr());
				flightDetail.setFlightno(flightNoList);
				String flightId = flightJson.getString("flight_id");
				flightDetail.setMonetaryunit(currency.get(flightId));
				flightDetail.setTax(flightTaxCost.get(flightId));
				flightDetail.setPrice(flightPriceCost.get(flightId));

				flightDetail.setWrapperid(param.getWrapperid());
				flight.setDetail(flightDetail);
				flight.setInfo(segs);
				flightList.add(flight);
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
