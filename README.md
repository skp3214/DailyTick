<img width="971" height="1280" alt="image" src="https://github.com/user-attachments/assets/b6a69d6a-1254-4bf6-8db5-3ce0d63464f9" /># DailyTick - Task Management App

![Android](https://img.shields.io/badge/Android-API%2021+-green.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-100%25-blue.svg)
![Room](https://img.shields.io/badge/Room-Database-orange.svg)
![MVVM](https://img.shields.io/badge/Architecture-MVVM-purple.svg)

A modern Android task management application built with Kotlin, featuring user authentication, task isolation, and a clean Material Design interface.

## 🚀 Features

### Core Functionality
- ✅ **Multi-User Support** - Each user has isolated task lists
- ✅ **Task Management** - Create, edit, delete, and mark tasks as complete
- ✅ **Priority System** - High, Medium, Low priority levels
- ✅ **Due Dates** - Optional due date assignment for tasks
- ✅ **Task Descriptions** - Optional detailed descriptions
- ✅ **Dark Mode Support** - Automatic theme switching

### Authentication
- 🔐 **User Registration** - Email-based account creation
- 🔐 **Secure Login** - Password-protected user accounts
- 🔐 **OTP Verification** - Mock OTP system for authentication
- 🔐 **Session Management** - Persistent login state
- 🔐 **Sign Out** - Clean session termination

### Technical Features
- 📱 **Room Database** - Local data persistence
- 🏗️ **MVVM Architecture** - Clean separation of concerns
- 🔄 **Coroutines** - Asynchronous operations
- 🎨 **Material Design** - Modern UI components
- 🌙 **Night Theme** - Enhanced dark mode experience

## 📱 Screenshots
<img width="971" height="1280" alt="image" src="https://github.com/user-attachments/assets/769de341-fbda-480a-a9b0-b3d5394e4ddd" />

### Authentication Flow
| Sign Up | Sign In | OTP Verification |
|---------|---------|------------------|
| User registration with email validation | Secure login for existing users | 6-digit OTP verification |

### Main App
| Home (Tasks) | Add Task | Settings |
|--------------|----------|----------|
| View pending and completed tasks | Create new tasks with priority and dates | User settings and sign out |

## 🏗️ Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture pattern with the following components:

```
┌─────────────────┐
│       UI        │ ← Fragments, Activities
├─────────────────┤
│   ViewModels    │ ← TaskViewModel, AuthViewModel
├─────────────────┤
│  Repositories   │ ← TaskRepository, UserRepository
├─────────────────┤
│      DAOs       │ ← TaskDao, UserDao
├─────────────────┤
│  Room Database  │ ← TaskDatabase
└─────────────────┘
```

### Key Components

#### Database Layer
- **TaskDatabase**: Room database with Task and User entities
- **TaskDao**: Data access object for task operations
- **UserDao**: Data access object for user operations
- **DateConverter**: Type converter for Date objects

#### Repository Layer
- **TaskRepository**: Business logic for task operations
- **UserRepository**: Business logic for user management

#### ViewModel Layer
- **TaskViewModel**: Manages task state and operations
- **AuthViewModel**: Handles authentication state and flow

#### UI Layer
- **MainActivity**: Main container with bottom navigation
- **AuthActivity**: Authentication flow container
- **Fragments**: HomeFragment, SettingsFragment, Auth fragments

## 🛠️ Tech Stack

| Category | Technology |
|----------|------------|
| **Language** | Kotlin |
| **UI Framework** | Android Views + View Binding |
| **Architecture** | MVVM |
| **Database** | Room |
| **Async Operations** | Kotlin Coroutines + Flow |
| **Navigation** | Fragment Navigation |
| **Design** | Material Design Components |

## 📋 Prerequisites

- Android Studio Arctic Fox (2020.3.1) or later
- Android SDK API 21 (Android 5.0) or higher
- Kotlin 1.8.0 or later

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/skp3214/DailyTick.git
cd DailyTick
```

### 2. Open in Android Studio
1. Open Android Studio
2. Click "Open an existing Android Studio project"
3. Navigate to the cloned directory and select it

### 3. Build and Run
1. Sync the project with Gradle files
2. Connect an Android device or start an emulator
3. Click "Run" or press `Ctrl+R`

## 📊 Database Schema

### User Entity
```kotlin
@Entity(tableName = "users")
data class User(
    @PrimaryKey val email: String,
    val password: String,
    val isLoggedIn: Boolean = false
)
```

### Task Entity
```kotlin
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String? = null,
    val priority: String = "LOW",
    val dueDate: Date? = null,
    val isCompleted: Boolean = false,
    val createdAt: Date = Date(),
    val completedAt: Date? = null,
    val userEmail: String
)
```

## 🔧 Configuration

### Database Version
Current database version: **7**

The app uses Room's `fallbackToDestructiveMigration(true)` for schema changes during development.

### Authentication
- **Mock OTP System**: Enter any 6-digit number for verification
- **Password Requirements**: Minimum 6 characters
- **Email Validation**: Standard email format validation

## 🎯 User Flow

### First Time User
1. **Launch App** → Splash screen → Sign Up
2. **Register** → Enter email, password, confirm password
3. **OTP Verification** → Enter any 6-digit number
4. **Home Screen** → Start creating tasks

### Returning User
1. **Launch App** → Splash screen → Auto-login (if logged in) or Sign In
2. **Sign In** → Enter registered email and password
3. **OTP Verification** → Enter any 6-digit number
4. **Home Screen** → View existing tasks

### Task Management
1. **Add Task** → Tap FAB → Fill title (required), description (optional), priority, due date (optional)
2. **Edit Task** → Tap on any task → Modify details
3. **Complete Task** → Tap checkbox → Task moves to completed section
4. **Delete Task** → Tap delete icon → Task removed permanently

## 🔒 Security Features

- **User Isolation**: Each user sees only their own tasks
- **Password Storage**: Stored in Room database (development setup)
- **Session Management**: Database-driven login state
- **Data Persistence**: All data stored locally in Room database

## 🐛 Known Issues & Solutions

### Common Issues

1. **App crashes when leaving description empty**
   - ✅ **Fixed**: Nullable description field with proper null handling

2. **Tasks shared between users**
   - ✅ **Fixed**: Added userEmail field for task isolation

3. **Deprecated API warnings**
   - ✅ **Fixed**: Updated to modern Android APIs with backward compatibility

4. **Platform declaration clash**
   - ✅ **Fixed**: Renamed conflicting method names

## 🔄 Recent Updates

### Version 2.0 (October 2025)
- ✅ **Complete Room Database Migration**: Replaced SharedPreferences with Room database
- ✅ **User Management System**: Added proper user authentication and isolation
- ✅ **Bug Fixes**: Resolved crashes with optional fields
- ✅ **Modern APIs**: Updated deprecated method usage
- ✅ **Dark Theme**: Enhanced night mode support

## 📚 Learning Outcomes

This project demonstrates:

- **Room Database**: Local data persistence with relationships
- **MVVM Architecture**: Clean separation of UI and business logic
- **Kotlin Coroutines**: Asynchronous programming
- **User Authentication**: Multi-user session management
- **Material Design**: Modern Android UI principles
- **State Management**: Reactive UI with StateFlow
- **Error Handling**: Graceful handling of edge cases

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

