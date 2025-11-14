from flask import Flask, jsonify, request
import requests

app = Flask(__name__)

API_KEY = "your-api--key"
BASE_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather"
AIR_POLLUTION_URL = "http://api.openweathermap.org/data/2.5/air_pollution"

def get_aqi_description(aqi):
    if aqi == 1:
        return "Good"
    elif aqi == 2:
        return "Fair"
    elif aqi == 3:
        return "Moderate"
    elif aqi == 4:
        return "Poor"
    elif aqi == 5:
        return "Very Poor"
    return "Unknown"

@app.route('/weather', methods=['GET'])
def get_weather():
    city = request.args.get('city')

    if not city:
        return jsonify({"error": "City parameter is required"}), 400

    if not API_KEY or API_KEY == "YOUR_OPENWEATHERMAP_API_KEY":
        return jsonify({"error": "API key is not configured on the server."}), 500

    try:
        # Get Current Weather and Coordinates
        weather_params = {
            'q': city,
            'appid': API_KEY,
            'units': 'metric'
        }
        weather_response = requests.get(BASE_WEATHER_URL, params=weather_params)
        weather_response.raise_for_status()
        weather_data = weather_response.json()

        lat = weather_data["coord"]["lat"]
        lon = weather_data["coord"]["lon"]

        # Get Air Pollution Data
        pollution_params = {
            'lat': lat,
            'lon': lon,
            'appid': API_KEY
        }
        pollution_response = requests.get(AIR_POLLUTION_URL, params=pollution_params)
        pollution_response.raise_for_status()
        pollution_data = pollution_response.json()

        # Extract the AQI value (1-5)
        aqi_value = pollution_data["list"][0]["main"]["aqi"]
        aqi_description = get_aqi_description(aqi_value)


    except requests.exceptions.HTTPError as err:
        if weather_response.status_code == 401:
             return jsonify({"error": "Invalid API key provided."}), 500
        if weather_response.status_code == 404:
            return jsonify({"error": f"Weather data not found for city: {city}"}), 404
        return jsonify({"error": f"An HTTP error occurred: {err}"}), 500
    except requests.exceptions.RequestException as err:
        return jsonify({"error": f"An error occurred fetching weather data: {err}"}), 500
    except Exception as e:
        return jsonify({"error": f"An error occurred processing data: {e}"}), 500

    # Combine Data and Send Response
    processed_data = {
        "city": weather_data["name"],
        "temperature": weather_data["main"]["temp"],
        "description": weather_data["weather"][0]["description"],
        "humidity": weather_data["main"]["humidity"],
        "aqi_description": aqi_description,
        "icon": weather_data["weather"][0]["icon"]
    }

    return jsonify(processed_data)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)