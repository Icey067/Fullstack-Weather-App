# 🌦️ Fullstack Weather App

A full-stack weather application using a Kotlin Android frontend and a Flask (Python) backend to deliver real-time weather forecasts from OpenWeatherMap.

## 📸 Screenshots

| Home Screen | Forecast View |
| :---: | :---: |
| ![App Home Screen](./assets/app.png) | ![App Forecast View](./assets/forecast.png) |

## ✨ Features

* **Current Weather:** Get real-time temperature, "feels like" temperature, humidity, and wind speed.
* **Forecast:** View the upcoming forecast for the searched location.
* **City Search:** Find weather data for any city.

## 🛠️ Tech Stack

#### Frontend (Kotlin)
* **Kotlin:** Core language for the Android app.
* **Jetpack Compose:** (Used for building the UI? If not, replace with XML).

#### Backend (Python)
* **Flask:** Lightweight Python framework for the REST API.
* **OpenWeatherMap API:** The external API used for weather data.

---

## 🚀 Getting Started

Follow these instructions to get a copy of the project up and running on your local machine.

### Prerequisites

* [Android Studio](https://developer.android.com/studio) (for the frontend)
* [Python 3.10+](https://www.python.org/) (for the backend)
* An **OpenWeatherMap API Key** (get one for free [here](https://openweathermap.org/home/sign_up))

### 1. Clone the Repository

```bash
git clone [https://github.com/Icey067/Fullstack-Weather-App.git](https://github.com/Icey067/Fullstack-Weather-App.git)
cd Fullstack-Weather-App
```

### 2. Backend Setup (Flask)
1) Navigate to the backend folder (e.g., cd backend):

```bash
cd backend
```
2) Create and activate a virtual environment:

```Bash

# Create venv
python -m venv venv

# Activate on Linux/macOS
source venv/bin/activate

# Activate on Windows
# .\venv\Scripts\activate
```

3) Install the required dependencies:

```Bash
pip install -r requirements.txt
```
(Note: If you don't have a requirements.txt file, you should create one with pip freeze > requirements.txt after installing Flask, requests, etc.)

4) Create a .env file in the backend directory and add your API key:

```
OPENWEATHER_API_KEY=your_secret_api_key_here
```
5) Run the Flask server:

```Bash

# Assumes your main file is app.py or main.py
python app.py
```

The backend server will now be running on http://127.0.0.1:5000

### 3. Frontend (Android) Setup

1) Open the /frontend (or root) folder in Android Studio.

2) Let Gradle build and sync the project.

3) Locate the file where you make your API call (e.g., ApiService.kt or Constants.kt).

$) Important: Change the BASE_URL to point to your local machine's IP address, not localhost.

  - Find your IP (e.g., 192.168.1.10).

  - Change the URL to http://192.168.1.10:5000/. (This is because the Android emulator runs in its own virtual machine and cannot access your computer's localhost directly.)

5) Build and run the app on an emulator or a physical device.

Would you like me to generate a good `.gitignore` file for a Kotlin + Python project?
