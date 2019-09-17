import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;


public class App {
	public static class Vehicle {
		   String Title;
		   int Price;
		   String Currency;
		   String Condition;
		   String Photos;
		   String Brand;
	}
	
	private static String listUrl = "https://api.mercadolibre.com/sites/MLU/search";
	private static String getVehicleUrl = "https://api.mercadolibre.com/items";
	private static int pages = 1;
	public static void main(String[] args) throws IOException, JSONException, UnirestException {
		ArrayList<String> ids = getIds(pages);
		ArrayList<Vehicle> vehicles = getVehicles(ids);
		int hola =3;
		hola = 2;
	}

	
	private static ArrayList<String> getIds(int pages) throws IOException, JSONException, UnirestException {
		ArrayList<String> ids = new ArrayList<String>();
		for (int i = 0; i < pages; i++)
		{
			HttpResponse<String> response = Unirest.get(listUrl + "?category=MLU1743&limit=50&offset=" + Integer.toString(50*pages))
					  .header("cache-control", "no-cache")
					  .header("Postman-Token", "9fa1dd7a-f255-42b7-8a66-66f53db5087d")
					  .asString();
			
			// print result
			JSONObject json = new JSONObject(response.getBody());
			JSONArray results = json.getJSONArray("results");
			
			for (int j = 0; j < results.length(); j++)
			{
				ids.add(results.getJSONObject(j).getString("id"));
			}
		}
		return ids;
	}
	
	private static ArrayList<Vehicle> getVehicles(ArrayList<String> ids) throws JSONException, IOException, UnirestException {
		ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
		for (int i = 0; i < ids.size(); i++)
		{
			HttpResponse<String> response = Unirest.get(getVehicleUrl + "?id=" + ids.get(i))
					  .header("Cache-Control", "no-cache")
					  .header("Postman-Token", "f23b7f9f-a231-48be-9e8f-f070519f48b1,0ec697a6-c146-4c9d-b72c-61bcbcc2a828")
					  .asString();

			JSONObject json = new JSONObject(response.getBody());
			//JSONArray attributes = json.getJSONArray("attributes");
			JSONArray pictures = json.getJSONArray("pictures");
			
			Vehicle vehicle = new Vehicle();
			vehicle.Title = json.getString("title");
			vehicle.Condition = json.getString("condition");
			vehicle.Currency = json.getString("currency_id");
			vehicle.Price = json.getInt("price");
			
			String picturesString = "";
			for (int j = 0; j < pictures.length(); j++)
			{
				picturesString += pictures.getJSONObject(j).getString("url") + ",";
			}
			vehicle.Photos = picturesString.substring(0, picturesString.length() - 1);
			vehicles.add(vehicle);
		}
		return vehicles;	
	}
}

