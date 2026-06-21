# Trip API Access Control Documentation

This document outlines the access control and permission rules for all Trip-related APIs in the application. It defines who can access, modify, or interact with different parts of a trip based on their assigned roles.

## Role Definitions

Access to trip data is governed by the following four roles:

1.  **Admin (Creator)**: The user who created the trip. This user has full control over all aspects of the trip, including editing details, deleting the trip, adding members, and managing all related data.
2.  **Accepted Member**: A user who was invited to the trip by the Admin and has accepted the invitation. They have elevated read/write access for collaborative features like expenses and media, but cannot modify the core trip details or manage other members.
3.  **Pending Member**: A user who has been invited to the trip but has not yet accepted the invitation. They generally do not have full member privileges until they accept.
4.  **Public User (Non-Member)**: Any authenticated user who is not the Admin and has not been invited to the trip. Their access is restricted to read-only views of trips explicitly marked as "Public" and media explicitly marked as "Public".

---

## Endpoint Permissions Breakdown

Below is the definitive list of permissions for each functional area of the trip.

### 1. Trip Core (`/trip`)

| Endpoint | Method | Required Role(s) | Description |
| :--- | :--- | :--- | :--- |
| `/createTrip` | `POST` | **Any Authenticated User** | Creates a new trip. The caller automatically becomes the **Admin**. |
| `/getTrips` | `GET` | **Any Authenticated User** | Returns a list of trips. Users will only see trips where they are the Admin or a Member. |
| `/getTrip/:tripId` | `GET` | **Admin**, **Accepted Member**, or **Public User** | Public Users can only view the trip if `tripVisibility` is set to "public". If it is "private", they receive a 404 error. |
| `/updateTrip/:tripId` | `PUT` | **Admin Only** | Only the trip creator can modify core trip details (name, dates, destination, visibility). |
| `/deleteTrip/:tripId` | `DELETE` | **Admin Only** | Only the trip creator can delete the trip. |

### 2. Trip Members (`/trip/:tripId/members`)

| Endpoint | Method | Required Role(s) | Description |
| :--- | :--- | :--- | :--- |
| `/:tripId/members` | `GET` | **Admin**, **Accepted Member**, or **Public User** | Public Users can view the member list only if the trip itself is "public". |
| `/:tripId/members` | `POST` | **Admin Only** | Only the trip creator can invite new users to the trip. |
| `/:tripId/acceptTrip`| `PATCH`| **Pending Member Only** | Only the specific user who was invited can accept their own invitation. |

### 3. Trip Media (`/trip/:tripId/media`)

*Note: Trip media (pictures/videos) is strictly confidential. Public Users cannot view media under any circumstances, even if the trip is public.*

| Endpoint | Method | Required Role(s) | Description |
| :--- | :--- | :--- | :--- |
| `/:tripId/media` | `POST` | **Admin** or **Accepted Member** | Both Admins and Accepted Members can upload media to the trip. |
| `/:tripId/media` | `GET` | **Admin** or **Accepted Member** | **Public Users** cannot view any media. **Admins and Members** can see `PUBLIC` media, their own `PRIVATE` media, and `SELECTED` media they have been granted access to. |

### 4. Trip Expenses & Settlements (`/trip/:tripId/expenses`, `/trip/:tripId/settlements`)

*Note: Financial data is strictly confidential and cannot be viewed by Public Users under any circumstances, regardless of trip visibility.*

| Endpoint | Method | Required Role(s) | Description |
| :--- | :--- | :--- | :--- |
| `/:tripId/initiateExpense` | `POST` | **Admin** or **Accepted Member** | Can initiate a new expense transaction. |
| `/:tripId/expenses` | `POST` | **Admin** or **Accepted Member** | Can add an expense to the trip ledger. |
| `/:tripId/expenses` | `GET` | **Admin** or **Accepted Member** | Can view the list of all trip expenses. |
| `/:tripId/settlements` | `GET` | **Admin** or **Accepted Member** | Can view the calculated settlements and balances. |

### 5. Itineraries & Activities

*Note: Itineraries are restricted to trip participants.*

| Endpoint | Method | Required Role(s) | Description |
| :--- | :--- | :--- | :--- |
| `/:tripId/itinerary` | `GET` | **Admin** or **Accepted Member** | Can view the detailed itinerary and schedule. **Public Users cannot view itineraries.** |
| `/:tripId/generateItinerary` | `POST` | **Admin** or **Accepted Member** | Both Admin and Accepted Members can generate or structure the core itinerary dates. |
| `/:tripId/activity` | `POST/PUT/DELETE`| **Admin** or **Accepted Member** | Both Admin and Accepted Members can add, update, or remove specific activities/spots from the itinerary. |

---

## Summary of Access Middleware & Utilities

*   **`authenticate`**: Ensures the user is logged into the application (Valid JWT). This is the baseline for all routes.
*   **`verifyTripAccess`**: A core utility that checks the `TripMemberToTrip` table. It returns `ADMIN`, `MEMBER`, or `NONE`. It is heavily used in controllers to block users with `NONE` status from accessing sensitive routes like Expenses and Itineraries.
*   **Visibility Checks**: For routes like `getTrip`, an additional code-level check is performed if the user's role is `PUBLIC` (or `NONE`), verifying `trip.tripVisibility === "public"`.
