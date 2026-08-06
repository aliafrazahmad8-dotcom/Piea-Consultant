# PIEA Student — Android App

Production-ready Android application for Punjab Institute of Education Abroad (PIEA) students.
Built with Kotlin, Jetpack Compose (Material 3), Firebase, Hilt, and MVVM architecture.

## What's included
- Login & Signup (Firebase Authentication)
- Student Dashboard
- Universities (list + detail, Firestore-backed)
- Scholarships
- Programs
- Online Admission Form (writes to Firestore)
- Document Upload (Firebase Storage)
- Application Tracking (per-user Firestore queries)
- Push Notifications (Firebase Cloud Messaging)
- WhatsApp Support (opens a chat with your support number)
- Google Maps Office Location
- Student Profile (view/edit, Firestore)
- Settings with Dark Mode toggle (persisted via DataStore)

## Architecture
MVVM: `data/model` (data classes) → `data/repository` (Firebase access, wrapped in a `Resource`
sealed class for Loading/Success/Error states) → `ui/screens/*/XViewModel` (Hilt-injected,
exposes `StateFlow`) → `ui/screens/*/XScreen` (Compose UI, stateless where possible).
Dependency injection via Hilt (`di/AppModule.kt`). Navigation via Navigation-Compose
(`navigation/NavGraph.kt`, `navigation/Screen.kt`).

## The only two things that need your own credentials

Everything in this project is finished code. There are exactly two account-specific values
that only you can provide (I cannot generate real credentials for your Firebase/Google Cloud
projects on your behalf):

1. **`app/google-services.json`** — a template is at `app/google-services.json.SAMPLE`.
   Create a Firebase project at console.firebase.google.com, add an Android app with package
   name `com.piea.student`, download the real `google-services.json` it gives you, and place
   it at `app/google-services.json` (replacing the sample). Then enable in the Firebase
   console: Authentication (Email/Password), Firestore Database, Storage, and Cloud Messaging.

2. **Google Maps API key** — in `app/build.gradle.kts`, replace
   `"YOUR_GOOGLE_MAPS_API_KEY"` with a Maps SDK for Android key from Google Cloud Console.

Also update the placeholder WhatsApp number in `utils/Constants.kt`
(`WHATSAPP_NUMBER`) and the office coordinates in
`ui/screens/map/OfficeLocationScreen.kt` (`OfficeLatLng`) with your real office location.

## Firestore collections the app expects
`users`, `universities`, `scholarships`, `programs`, `applications`, `notifications`,
plus a `documents` subcollection under each user. Populate `universities`, `scholarships`,
and `programs` from the Firebase console (or your own admin tool) — the app reads them live.

## Opening the project
Open the `PieaStudent` folder in Android Studio (Koala or newer), let Gradle sync, drop in
your `google-services.json`, and Run. Minimum SDK 24, target/compile SDK 34.
