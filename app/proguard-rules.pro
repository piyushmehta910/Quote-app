# ProGuard rules for Quote App

# Keep Compose
-dontwarn androidx.compose.**
-keep class androidx.compose.** { *; }

# Keep Navigation
-keepnames class * extends android.os.Parcelable
-keepnames class * extends java.io.Serializable

# Keep DataStore
-keep class * extends androidx.datastore.preferences.protobuf.GeneratedMessageLite { *; }

# Keep Coil
-dontwarn coil.**
-keep class coil.** { *; }
