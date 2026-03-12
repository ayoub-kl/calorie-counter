# Architecture Notes

## Recommended baseline

- Android app in Kotlin
- Jetpack Compose for UI
- Hilt for DI
- Navigation Compose for routing
- CameraX for meal photo capture
- Retrofit + OkHttp for backend calls
- Room for local persistence
- Coroutines + Flow for async and state

## Layers

### UI
Composable screens and reusable components.

### Presentation
ViewModels exposing immutable state and orchestrating use cases.

### Domain
Use cases and business logic such as meal analysis normalization and totals calculation.

### Data
Repositories, Retrofit services, DTOs, Room entities/DAOs.

## Key rules

- DTO != domain model != Room entity
- no network/database calls in composables
- no nutrition business logic in composables
- keep AI output validation in data/domain boundaries
