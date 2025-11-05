from flask import Flask, jsonify, request
import requests

app = Flask(__name__)

API_KEY = "your_api_key"
BASE_WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather"

@app.route('/weather', methods=['GET'])
def get_weather():
    city = request.args.get('city')

    if not city:
        return jsonify({"error": "City parameter is required"}), 400

    if not API_KEY or API_KEY == "YOUR_OPENWEATHERMAP_API_KEY":
        return jsonify({"error": "API key is not configured on the server."}), 500

    try:
        params = {
            'q': city,
            'appid': API_KEY,
            'units': 'metric'
        }
        response = requests.get(BASE_WEATHER_URL, params=params)
        response.raise_for_status()

    except requests.exceptions.HTTPError as err:
        if response.status_code == 401:
             return jsonify({"error": "Invalid API key provided."}), 500
        if response.status_code == 404:
            return jsonify({"error": f"Weather data not found for city: {city}"}), 404
        return jsonify({"error": f"An HTTP error occurred: {err}"}), 500
    except requests.exceptions.RequestException as err:
        return jsonify({"error": f"An error occurred fetching weather data: {err}"}), 500

    weather_data = response.json()

    processed_data = {
        "city": weather_data["name"],
        "temperature": weather_data["main"]["temp"],
        "description": weather_data["weather"][0]["description"],
        "humidity": weather_data["main"]["humidity"],
        "wind_speed": weather_data["wind"]["speed"],
        "icon": weather_data["weather"][0]["icon"]
    }

    return jsonify(processed_data)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)

