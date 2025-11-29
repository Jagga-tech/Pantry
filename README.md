# Pantrypal - Your AI-Powered Kitchen Companion 🍳

Hey there! Welcome to Pantrypal, the smartest way to manage your kitchen, track nutrition, and discover recipes you'll love.

---

## 🎯 What is Pantrypal?

You know that feeling when you open your fridge and think "I have no idea what to cook"? Or when you find that tomato that's been hiding in the back for way too long? Yeah, we've all been there.

**Pantrypal is your personal kitchen assistant that:**
- 🤖 Recommends recipes based on what you actually have at home
- 🥗 Respects your dietary preferences (Vegetarian, Vegan, Keto, etc.)
- 📊 Tracks your nutrition goals and daily intake
- 🗓️ Helps you plan meals for the week
- ⏰ Alerts you before ingredients expire
- 💪 Helps you achieve your health goals

Think of it as having a smart nutritionist and chef who knows exactly what's in your kitchen and suggests personalized meals that fit your diet and health goals.

---

## ✨ Core Features

### 🤖 **AI-Powered Recipe Recommendations**
Our intelligent recommendation algorithm scores recipes based on:
- **Pantry Match** (0-50 points) - How many ingredients you already have
- **Dietary Fit** (0-30 points) - Matches your dietary preferences perfectly
- **Nutrition Alignment** (0-20 points) - Fits your remaining calorie budget

**Example:** If you're vegetarian with 800 calories left, we'll recommend "Veggie Stir Fry" (Score: 85) over "Beef Stew" (Score: 10).

### 📦 **Smart Pantry Management**
- Track all your ingredients in one place
- See what's expiring soon with personalized alerts
- Get dietary-specific item suggestions (e.g., "Tofu for your Vegan diet")
- Quick stats: Total items, expiring count, categories

### 🥗 **Complete User Personalization**
Every screen adapts to YOU:
- **Home:** Personalized greeting, recipes matching your diet + pantry
- **Profile:** Your info, dietary badge, nutrition dashboard
- **Pantry:** Custom suggestions based on your dietary preference
- **Browse:** Filter recipes for your diet with live count

### 📊 **Nutrition Tracking & Goals**
Track your daily nutrition with beautiful progress bars:
- **Calories:** See progress toward your daily goal
- **Protein:** Track your protein intake
- **Carbs & Fats:** Complete macro tracking
- **Auto-Reset:** Automatically resets at midnight
- **Set Custom Goals:** Personalized calorie and macro targets

### 🗓️ **Meal Planning System**
Plan your entire week with smart suggestions:
- Weekly, daily, or custom meal plans
- Assign recipes to specific days and meals
- Track total calories for the week
- Filter by dietary restrictions automatically
- Syncs to Firebase in real-time

### 🎨 **Beautiful, Personalized UI**
- **Dynamic greetings** based on time of day
- **User profile pictures** throughout the app
- **Dietary preference badges** showing your diet
- **Real-time stats** (pantry count, favorites, expiring items)
- **Progress indicators** for nutrition goals

---

## 🎯 Who Is This For?

Perfect if you:
- ❤️ Care about your health and nutrition
- 🌱 Follow a specific diet (Vegetarian, Vegan, Keto, etc.)
- 💰 Want to save money by reducing food waste
- 🏋️ Have fitness/nutrition goals
- 👨‍🍳 Love cooking but need inspiration
- 📱 Want a modern, personalized app experience

---

## 🚀 Key Features Breakdown

### 1️⃣ Intelligent Recommendations
```
Algorithm considers:
✓ Ingredients you have (90% match = "You can make this!")
✓ Your dietary preference (Vegetarian recipes for vegetarians)
✓ Your nutrition goals (Fits your remaining calories)
✓ Recipe difficulty and cooking time

Result: Top 10 recipes ranked by personalization score
```

### 2️⃣ Dietary Preferences Support
**Supported Diets:**
- 🥬 Vegetarian
- 🌱 Vegan
- 🍞 Gluten-Free
- 🥑 Keto
- 🍖 Paleo
- 🕌 Halal
- ✡️ Kosher
- 🍽️ None (All recipes)

**Features:**
- Set during signup or change anytime in Profile
- Filters recipes across the entire app
- Shows dietary-appropriate pantry suggestions
- Badges display your diet on your profile

### 3️⃣ Nutrition Dashboard
Located in **Profile Fragment**:
- Today's nutrition summary
- Visual progress bars for all macros
- Remaining calories/protein/carbs/fat
- "Set Nutrition Goals" button for customization

**Default Goals:**
- 2000 calories
- 50g protein
- 250g carbs
- 70g fat

### 4️⃣ Real-Time Personalization
**HomeFragment:**
- "Good Morning, [Your Name]!"
- Dietary preference shown if set
- Expiring items: "[Name], these items are expiring soon"
- Recipe suggestions based on your pantry + diet

**PantryFragment:**
- "Hi [Name], you have X items in your pantry"
- "Suggested items for your [Diet] diet: Tofu, Legumes..."
- Personalized expiring alerts

**BrowseFragment:**
- "Recipes for my [Diet] diet" filter chip
- Live count: "12 matching recipes"
- Sorted by recommendation score

---

## 🏗️ Technical Architecture

### **Design Pattern**
- **MVVM Architecture** (Model-View-ViewModel)
- **Repository Pattern** for data access
- **LiveData** for reactive UI updates
- **Firebase integration** for real-time sync

### **Tech Stack**
```
Language:     Java (Android)
Min SDK:      API 24 (Android 7.0)
Database:     Room (local) + Firestore (cloud)
Auth:         Firebase Authentication
UI:           Material Design 3
Navigation:   Navigation Component
Binding:      ViewBinding
```

### **Firebase Services**
- **Authentication:** Email/Password + Google Sign-In
- **Firestore Collections:**
  - `users/` - User profiles with nutrition data
  - `users/{userId}/pantryItems/` - User's pantry
  - `users/{userId}/favorites/` - Favorite recipes
  - `meal_plans/` - Weekly meal plans
- **Real-time Sync:** All data updates instantly across devices

### **Key Components**

#### **Data Models**
- `User.java` - User profile + nutrition tracking
- `Recipe.java` - Recipe data with nutrition info
- `PantryItem.java` - Pantry ingredients
- `MealPlan.java` - Weekly meal planning

#### **ViewModels**
- `UserViewModel` - User data + nutrition
- `RecipeViewModel` - Recipes + favorites
- `PantryItemViewModel` - Pantry management

#### **Services**
- `RecipeRecommendationService` - AI recommendation algorithm
- `FirebaseAuthManager` - Authentication handling

#### **Repositories**
- `FirebaseUserRepository` - User CRUD operations
- `FirebasePantryRepository` - Pantry management
- `FirebaseFavoritesRepository` - Favorites tracking
- `FirebaseMealPlanRepository` - Meal planning
- `RecipeRepository` - Local recipe database

---

## 📱 App Screens

### **Authentication Flow**
1. **MainActivity** → Splash screen
2. **OnboardingActivity** → First-time user introduction
3. **SignUpActivity** → Create account with dietary preference
4. **LoginActivity** → Email/password login

### **Main Navigation (Bottom Nav)**
1. **Home** 🏠
   - Personalized greeting
   - Quick stats (pantry count, favorites)
   - Expiring items alert
   - AI-recommended recipes

2. **Favorites** ⭐
   - Grid view of saved recipes
   - Synced with Firebase
   - Empty state for new users

3. **Pantry** 📦
   - List of all ingredients
   - Expiring items section
   - Dietary suggestions
   - Quick stats

4. **Profile** 👤
   - User info (name, email, profile pic)
   - Dietary preference badge
   - Member since date
   - **Edit Profile** with name + diet
   - **Nutrition Dashboard** with goals
   - Change password
   - Sign out

### **Secondary Screens**
- **Recipe Detail** - Full recipe with tabs (Ingredients, Instructions, Nutrition)
- **Browse Recipes** - Search + filter by category + dietary preference
- **Add Pantry Item** - Add ingredients with expiration dates

---

## 🎨 Design System

### **Color Palette**
```
Primary:     #4CAF50 (Fresh Green)
Secondary:   #FF9800 (Warm Orange)
Accent:      #9C27B0 (Purple)
Error:       #F44336 (Red)
Background:  #F5F5F5 (Light Gray)
```

### **Typography**
- Headings: Bold, 18-24sp
- Body: Regular, 14-16sp
- Captions: 10-12sp

### **Components**
- Material Cards with elevation
- Circular profile pictures
- Progress bars for nutrition
- Filter chips for categories
- FABs for quick actions
- Bottom sheets for details

---

## 🔥 Advanced Features

### **Smart Algorithm Scoring**
```java
Recipe Score = PantryMatch + DietaryFit + NutritionAlignment

Example:
"Veggie Stir Fry" for Vegetarian with 800 cal remaining:
- Pantry: 45/50 (90% ingredients available)
- Dietary: 30/30 (Perfect vegetarian match)
- Nutrition: 15/20 (Fits calorie budget)
= 90/100 Total Score ⭐⭐⭐⭐⭐
```

### **Automatic Nutrition Reset**
```java
// Checks daily at app launch
if (lastResetDate < today) {
    user.resetDailyNutrition();
    // Calories, protein, carbs, fat → 0
}
```

### **Missing Ingredients Detection**
```java
List<String> missing = RecipeRecommendationService
    .getMissingIngredients(recipe, pantryItems);

// Output: ["Onions", "Garlic", "Soy Sauce"]
// User can add these to shopping list
```

---

## 📊 Data Flow

```
User Action → ViewModel → Repository → Firebase
                ↓
            LiveData Observer
                ↓
            UI Updates Automatically
```

**Example:** User updates dietary preference
```
1. ProfileFragment: user.setDietaryPreference("Vegan")
2. UserViewModel.updateUser(user)
3. FirebaseUserRepository saves to Firestore
4. LiveData observer notifies all fragments
5. HomeFragment auto-updates recipe suggestions
6. BrowseFragment shows "Recipes for my Vegan diet"
7. PantryFragment shows vegan item suggestions
```

---

## 🔐 Security & Privacy

- **Firebase Authentication** - Secure user accounts
- **Password Reset** - Email-based password recovery
- **Data Encryption** - Firebase handles encryption at rest
- **User Isolation** - Each user only sees their own data
- **No Password Storage** - Passwords never stored locally
- **Real-time Rules** - Firestore security rules enforce access

---

## 🛣️ Roadmap

### ✅ **Completed**
- User authentication & profiles
- Pantry management with expiration tracking
- Recipe database with favorites
- Dietary preference system
- AI-powered recommendations
- Nutrition tracking dashboard
- Meal planning infrastructure
- Complete personalization across all screens

### 🚧 **Coming Soon**
- [ ] Meal Planning UI (calendar view)
- [ ] Shopping list generation
- [ ] Barcode scanner for adding items
- [ ] Recipe upload by users
- [ ] Social features (share recipes)
- [ ] Dark mode
- [ ] Offline mode
- [ ] Widget for quick pantry view

### 💡 **Future Ideas**
- Recipe rating system
- Cooking timers
- Voice commands
- AR for measuring portions
- Integration with smart fridges
- Meal prep suggestions
- Cost tracking per recipe

---

## 🧑‍💻 For Developers

### **Prerequisites**
```
- Android Studio Arctic Fox or later
- JDK 11+
- Android SDK API 24+
- Firebase account
```

### **Setup Instructions**

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/pantrypal.git
cd pantrypal
```

2. **Configure Firebase**
- Create a Firebase project at [console.firebase.google.com](https://console.firebase.google.com)
- Add Android app with package: `com.pantrypal`
- Download `google-services.json`
- Place in `app/` directory

3. **Enable Firebase Services**
- Authentication (Email/Password + Google)
- Firestore Database
- Create Firestore indexes if needed

4. **Build and Run**
```bash
./gradlew assembleDebug
```

### **Project Structure**
```
app/src/main/java/com/pantrypal/
├── data/
│   ├── model/           # Data classes (User, Recipe, etc.)
│   ├── repository/      # Firebase repositories
│   ├── service/         # Business logic (Recommendations)
│   ├── database/        # Room database
│   └── firebase/        # Firebase managers
├── ui/
│   ├── home/           # Home screen
│   ├── profile/        # Profile + Nutrition
│   ├── pantry/         # Pantry management
│   ├── browse/         # Recipe browsing
│   ├── recipe/         # Recipe details
│   ├── auth/           # Login/Signup
│   └── viewmodel/      # ViewModels
└── util/               # Utilities & helpers
```

### **Key Classes**
- `RecipeRecommendationService` - Core recommendation algorithm
- `FirebaseUserRepository` - User data management
- `User` - User model with nutrition tracking
- `MealPlan` - Meal planning model
- `HomeFragment` - Personalized dashboard

---

## 🤝 Contributing

We welcome contributions! Here's how:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

**Coding Standards:**
- Follow Android best practices
- Use ViewBinding (not findViewById)
- Write clean, commented code
- Test on multiple devices
- Follow Material Design guidelines

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- **Material Design 3** for beautiful UI components
- **Firebase** for backend infrastructure
- **RecyclerView** for smooth scrolling
- **LiveData** for reactive updates
- **Navigation Component** for seamless navigation

---

## 📧 Contact

**Developer:** Chanpreet Singh
**Email:** your.email@example.com
**Project Link:** https://github.com/yourusername/pantrypal

---

## 🎉 Fun Stats

```
📦 Total Features:     15+ major features
🎨 UI Screens:         10+ screens
💾 Data Models:        6 main models
🔥 Firebase Collections: 4 collections
📊 ViewModels:         4 ViewModels
🤖 AI Algorithm:       100-point scoring system
⭐ Dietary Options:    8 diet types
📈 Nutrition Tracking: 4 macros tracked
```

---

## 🌟 Screenshots

*Coming soon! Screenshots of the beautiful UI will be added here.*

---

**Happy Cooking! 👨‍🍳👩‍🍳**

Remember: The best meal is the one that fits your diet, uses what you have, and helps you reach your health goals. Let's make that easier!

---

**Last Updated:** January 2025
**Version:** 2.0.0
**Status:** Feature-Complete 🚀
