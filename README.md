# Pantrypal - Recipe & Pantry Management Android App

A modern, professional Android application that helps users discover recipes based on available ingredients, manage their pantry, track expiring items, and save favorites.

## 📱 Project Overview

**Pantrypal** is built with Material Design 3 (Material You) and follows the MVVM architecture pattern. The app provides a comprehensive solution for recipe discovery and pantry management.

### Key Features
- 🔍 **Ingredient-based Recipe Search** - Find recipes based on what you have
- 📦 **Pantry Management** - Track your ingredients and their expiration dates
- ❤️ **Save Favorites** - Mark your favorite recipes for quick access
- ⚠️ **Expiration Alerts** - Get notified about items expiring soon
- 🎨 **Beautiful UI** - Material Design 3 with custom color scheme

## 🛠️ Technology Stack

- **Language**: Java
- **Min SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room ORM
- **UI Framework**: Material Design 3
- **Image Loading**: Glide
- **HTTP Client**: Retrofit (for future API integration)
- **Build Tool**: Gradle

## 📐 Design System

### Colors
- **Primary**: Green (#10B981)
- **Secondary**: Orange (#F97316)
- **Accent**: Purple (#8B5CF6)
- **Text Primary**: Dark Gray (#1F2937)
- **Text Secondary**: Gray (#6B7280)
- **Background**: Light Gray (#F9FAFB)

### Typography
- Headers: 24-36sp, Bold
- Body: 14-16sp, Regular
- Small: 12sp, Regular

### Spacing
- Uses 8dp grid system (8, 16, 24, 32dp)

### Corner Radius
- Small components: 8dp
- Medium components: 12dp
- Large components: 20dp

## 📁 Project Structure

```
app/src/main/
├── java/com/pantrypal/
│   ├── MainActivity                    # Entry point with splash screen
│   ├── data/
│   │   ├── model/                     # Data models
│   │   │   ├── User.java
│   │   │   ├── Recipe.java
│   │   │   ├── PantryItem.java
│   │   │   └── Ingredient.java
│   │   ├── dao/                       # Room DAOs
│   │   │   ├── UserDao.java
│   │   │   ├── RecipeDao.java
│   │   │   ├── PantryItemDao.java
│   │   │   └── IngredientDao.java
│   │   ├── database/
│   │   │   └── PantrypalDatabase.java # Room database
│   │   └── repository/                # Data repositories
│   │       ├── UserRepository.java
│   │       ├── RecipeRepository.java
│   │       ├── PantryItemRepository.java
│   │       └── IngredientRepository.java
│   ├── ui/
│   │   ├── viewmodel/                 # ViewModels
│   │   │   ├── UserViewModel.java
│   │   │   ├── RecipeViewModel.java
│   │   │   └── PantryItemViewModel.java
│   │   ├── auth/                      # Authentication screens
│   │   │   ├── LoginActivity.java
│   │   │   └── SignUpActivity.java
│   │   ├── onboarding/                # Onboarding screens
│   │   │   ├── OnboardingActivity.java
│   │   │   └── fragments/
│   │   ├── home/                      # Home screens
│   │   │   ├── HomeActivity.java
│   │   │   └── HomeFragment.java
│   │   ├── recipe/                    # Recipe detail screens
│   │   ├── pantry/                    # Pantry management screens
│   │   ├── favorites/                 # Favorites screen
│   │   ├── profile/                   # Profile & settings screen
│   │   └── browse/                    # Browse recipes screen
│   └── util/
│       └── SharedPreferencesManager.java
├── res/
│   ├── layout/                        # Layout files
│   ├── drawable/                      # Vector drawables
│   ├── values/
│   │   ├── colors.xml
│   │   ├── strings.xml
│   │   ├── dimens.xml
│   │   └── themes.xml
│   ├── navigation/
│   │   └── nav_graph.xml
│   └── menu/
│       ├── menu_main.xml
│       └── menu_bottom_navigation.xml
└── AndroidManifest.xml
```

## 🎯 Screen Breakdown (13 MVP Screens)

### 1. **Splash Screen**
- Loading animation
- App branding with gradient background
- Auto-navigation based on user state

### 2. **Onboarding** (3 pages)
- Feature introduction
- Skip/Get Started buttons
- Page indicators

### 3. **Login Screen**
- Email/Password input with validation
- Social login buttons
- Sign up link

### 4. **Sign Up Screen**
- Full registration form
- Dietary preferences selection
- Terms & Conditions agreement

### 5. **Home Screen (Dashboard)**
- User greeting
- Search bar
- Stats cards (Pantry items, Saved recipes)
- Quick action buttons
- Recipe suggestions
- Popular recipes grid

### 6. **Ingredient Input Screen**
- Search & select ingredients
- Common ingredient chips
- Selected ingredients display

### 7. **Recipe Search Results**
- Grid of matching recipes
- Sort & filter options
- Empty state handling

### 8. **Recipe Detail Screen**
- Hero image with parallax
- Tabbed interface (Ingredients, Instructions, Nutrition)
- Save to favorites
- Share option

### 9. **Browse All Recipes**
- Category filters
- Search functionality
- Sort options
- Infinite scroll

### 10. **My Favorites**
- Grid of saved recipes
- Search within favorites
- Remove from favorites

### 11. **My Pantry**
- Items grouped by category
- Expiration tracking
- Edit/Delete options
- Add new item

### 12. **Add/Edit Pantry Item**
- Form with autocomplete
- Date picker for expiration
- Category selection

### 13. **Profile & Settings**
- User information display
- Account settings
- Notification preferences
- Logout option

## 🚀 Getting Started

### Prerequisites
- Android Studio 2024.1.4 or higher
- Android SDK 34
- Java 17+

### Build Instructions

1. **Clone/Open the project**
   ```bash
   cd /Users/chanpreetsingh/StudioProjects/Pantry
   ```

2. **Build the project**
   ```bash
   ./gradlew build
   ```

3. **Run on emulator/device**
   ```bash
   ./gradlew installDebug
   ```

### File Structure Overview
- `settings.gradle` - Project settings and module configuration
- `build.gradle` (top-level) - Plugin versions
- `app/build.gradle` - App dependencies and build configuration
- `app/src/main/AndroidManifest.xml` - App manifest

## 📦 Dependencies

### Core Android
- androidx.appcompat:appcompat:1.6.1
- androidx.core:core:1.12.0
- com.google.android.material:material:1.11.0

### Architecture
- androidx.lifecycle:lifecycle-viewmodel:2.6.2
- androidx.lifecycle:lifecycle-livedata:2.6.2
- androidx.room:room-runtime:2.6.1

### UI
- androidx.viewpager2:viewpager2:1.0.0
- androidx.recyclerview:recyclerview:1.3.2
- com.tbuonomo:dotsindicator:4.3

### Image Loading
- com.github.bumptech.glide:glide:4.16.0

### Networking
- com.squareup.retrofit2:retrofit:2.10.0
- com.google.code.gson:gson:2.10.1

## 🎨 Design System Implementation

### Color Usage
- Primary Green: CTAs, navigation, highlights
- Secondary Orange: Alerts, warnings, accents
- Purple: Favorites, premium features
- Grays: Text hierarchy, backgrounds

### Spacing System
All components follow the 8dp grid:
- Padding/Margin: 8, 16, 24, 32dp
- Component sizes use multiples of 8dp

### Typography Hierarchy
- Large titles: 28-36sp, bold
- Section headers: 20-24sp, bold
- Body text: 14-16sp, regular
- Labels: 12-14sp, medium

## 🔄 Data Flow (MVVM)

1. **UI Layer** (Activities/Fragments) → Observes LiveData from ViewModel
2. **ViewModel** → Communicates with Repository
3. **Repository** → Manages data from Room Database & APIs
4. **Data Layer** → Room Database & Network requests

## 🔐 Security Notes

- Passwords stored in Room database (should be hashed in production)
- SharedPreferences for session management
- Android manifest includes necessary permissions:
  - INTERNET (for API calls)
  - READ_EXTERNAL_STORAGE (for image access)
  - CAMERA (for future QR scanning)

## 📝 Future Enhancements

- [ ] Backend API integration with Retrofit
- [ ] Push notifications for expiring items
- [ ] QR code scanning for ingredients
- [ ] Meal planning feature
- [ ] Shopping list generation
- [ ] Recipe rating & reviews
- [ ] Social features (share, follow)
- [ ] AI-powered recipe recommendations

## 📄 License

This project is proprietary and for demonstration purposes.

## 👨‍💻 Author

Chanpreet Singh - Android Developer

---

**Built with ❤️ using Material Design 3**