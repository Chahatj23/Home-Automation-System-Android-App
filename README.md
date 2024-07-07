
# 🏠 Home Automation System

## Overview

Welcome to the **Home Automation System**! This Android app is designed to make managing your smart home devices a breeze. Capture and upload images, manage user data, and receive real-time notifications—all from one convenient app.

## ✨ Features

- 🔒 **User Authentication**: Seamlessly authenticate users with Firebase Authentication.
- 📸 **Image Capture and Upload**: Easily capture images and upload them to Firebase Storage.
- 🗄️ **User Data Management**: Store and manage user information and images in Firestore.
- 🔔 **Push Notifications**: Stay updated with real-time notifications via Parse Server (Back4App).
- 📱 **Dynamic Interface**: Enjoy a responsive and intuitive user interface.

## 🚀 Getting Started

### Prerequisites

- 🛠️ Android Studio
- 🔥 Firebase account
- 🌐 Back4App account

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/home-automation-system.git
   cd home-automation-system
   ```

2. **Open the project in Android Studio**:
   - Launch Android Studio.
   - Select `Open an existing Android Studio project`.
   - Navigate to the cloned repository folder and select it.

3. **Set up Firebase**:
   - Go to the [Firebase Console](https://console.firebase.google.com/).
   - Create a new project or use an existing project.
   - Add an Android app to your Firebase project.
   - Register the app with your package name (`com.example.homeautomationsystem`).
   - Download the `google-services.json` file and place it in the `app/` directory of your project.
   - Enable Firebase Authentication, Firestore, and Storage in the Firebase Console.

4. **Set up Back4App**:
   - Go to the [Back4App Dashboard](https://dashboard.back4app.com/).
   - Create a new app or use an existing app.
   - Obtain the `Application ID` and `Client Key` from the app settings.
   - Initialize Back4App in your project by adding the following to your `MainActivity`:
     ```java
     Parse.initialize(new Parse.Configuration.Builder(this)
             .applicationId("YOUR_APP_ID")
             .clientKey("YOUR_CLIENT_KEY")
             .server("https://parseapi.back4app.com/")
             .build()
     );
     ```

### 📜 Permissions

Ensure that the following permissions are declared in your `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.CAMERA"/>
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

### 📂 Firebase Storage Rules

Make sure your Firebase Storage rules allow authenticated users to upload files:
```plaintext
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

## 💡 Usage

### MainActivity

- 📷 **Image Capture**: Click the "Capture Image" button to open the camera and take a picture.
- ⬆️ **Image Upload**: Click the "Upload Image" button to upload the captured image to Firebase Storage.
- 👤 **User Information**: Displays the user's name at the top of the activity.

### SetupActivity

- 📝 **User Setup**: Allows users to input their details (flat number, floor, block, name, phone number, device ID) during the initial setup.

### MenuActivity

- 📱 **Device Information**: Displays the device ID and connection status with an animated card showing the device image.

## 🤝 Contributing

Contributions are welcome! Please submit a pull request or open an issue to discuss your ideas.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📧 Contact

For any inquiries, please contact [chahatj06@gmail.com](mailto:chahatj06@gmail.com)
