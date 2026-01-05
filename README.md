# 🎨 SayToons - AI Routine Helper for Kids

![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=android&logoColor=white)
![AI Powered](https://img.shields.io/badge/AI%20Gen-Pollinations-ff0000?style=for-the-badge)

**SayToons** is an interactive Android application designed to help children follow daily routines (like packing bags, brushing teeth) using Voice Recognition and Generative AI rewards.

---

## ✨ Features

- **🗣️ Voice-Activated Tasks:** The app listens for children to say specific phrases (e.g., "I packed my bag") to complete tasks.
- **🎨 AI Reward System:** Instantly generates unique, cute cartoons using **Pollinations.ai** as a reward for completing routines.
- **🔐 Parental Controls:** - PIN/Math challenge to access parent settings.
    - **Screen Time Manager:** Auto-locks the app after 15 minutes to prevent addiction.
    - **Guest Mode:** Try the app without logging in (data not saved).
- **🏠 Routine Management:** Pre-set routines for Morning, School, Meals, and Bedtime.
- **🌙 "Bye Bye" Mode:** A calm, animated screen that tells the child to sleep when screen time is over.

---

## 🛠️ Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material3)
- **Architecture:** MVVM (Model-View-ViewModel)
- **AI Generation:** Pollinations.ai (Turbo Model)
- **Speech Recognition:** Android SpeechRecognizer
- **Image Loading:** Coil (with GIF support)
- **Backend:** Firebase (Auth & Firestore) for syncing progress.

---

## 🚀 How to Run

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/yourusername/SayToons.git](https://github.com/yourusername/SayToons.git)

## Firebase Setup:

1. **Go to your Firebase Console.**

2. Download the google-services.json file.

3. Place it in the app/ directory of the project.

4. API Keys Configuration:

5. Create a local.properties file in the root directory (if it doesn't exist).

5. Add your Pollinations API key (if using a private tier) and SDK path:

## Properties

```bash
sdk.dir=/path/to/your/android/sdk
POLLINATIONS_API_KEY="your_actual_api_key_here"
(Note: If using the free public tier of Pollinations, the key might not be strictly necessary depending on implementation, but the config should exist).
```
## Build and Run:

1. Open the project in Android Studio.

2. Sync Gradle files.

3. Select an emulator or physical device and click Run.

## ⚙️ Configuration
**Screen Time Settings**

1. The session duration and lock times are managed in com.saytoons.app.ScreenTimeManager.kt.

Kotlin


// Example configuration in ScreenTimeManager.kt
// PRODUCTION SETTINGS:
```bash
private const val SESSION_DURATION_MS = 15 * 60 * 1000L // 15 Minutes active time
private const val LOCKOUT_DURATION_MS = 15 * 60 * 1000L // 15 Minutes lockout duration
To reset a locked state during development, uninstall the app to clear SharedPreferences.
```

Created with ❤️ for kids everywhere.
