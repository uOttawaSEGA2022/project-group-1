# Classes and their Responsabilities #

A description of classes and their roles.

## Model Classes ##

Model classes should be able to safely block.
As such, the Activities must communicate with them asynchronously.

### MealerSystem ###
Signature: `final class MealerSystem extends Application`

Description: Single point of access for many resources 
Stores global variables such as the current user's session data, the current IRepository implementation instance, etc.

Responsabilities:
- Provide initialization of the application state
- Provide access to global variables (Current user, Repository, itself)
- Log in and log out (change current user, UI must still check and update itself)
- Restore the previous user session if they did not logout

### ISerialisableEntity ###
Signature: `interface ISerialisableEntity`

Description: Generalization of an Entity that can serialise and deserialise its own state.

Responsabilities:
- Provide serialisation to a JSON-compatible Map
- Provide deserialisation from a JSON-compatible Map

### IRepositoryEntity ###
Signature: `interface IRepositoryEntity extends ISerialisableEntity`

Description: Specialization of `ISerialisableEntity` that can be stored to and retrieved from a table in permanent storage.

Responsabilities:
- Store a mapping of Entities to their table names
- Provide an ID to look up specific entities

Notable variable:
- `Map<Class<? extends Entity>, String> tableNames` (provides table names for each subclass of Entity. **MUST BE UPDATED MANUALLY**)

### User ###
Signature: `final class User extends IRepositoryEntity`

Description: An implementation of IRepositoryEntity that stores user data common to all user types.

Responsabilities:
- Store and retrieve user data
- Allow the creation of new users in the Repository
- Establish a connection to its UserRole
- Serialise and deserialise its UserRole when necessary

State:
- `String firstName, lastName, email, address`
- `String passwordHash` (*Password is hashed for at least a minimum of security*)
- `boolean admin` (Used as a flag. Should be set to true when the User has no UserRole upon creation)
- `UserRole role` (May be null if the user is an Administrator)

### UserRole ###
Signature: `abstract class UserRole extends ISerialisableEntity`

Description: Generalization of Roles a User account can possess.

Responsabilities:
- Return its User

### ClientRole ###
Signature: `final class ClientRole extends UserRole`

Description: Stores user data for clients.

Responsabilities:
- Store and retrieve the client's credit card number

### CookRole ###
Signature: `final class CookRole extends UserRole`

Description: Stores user data for cooks.

Responsabilities:
- Store and retrieve the cook's self-description
- Store and retrieve ban information


Ban Details:

CookRole contains banReason which is set by the admin and displayed to the cook.

It also contains banExpiration which acts as the ban indicator:
- banExpiration = -1 => The cook is not banned
- banExpiration =  0 => The cook is permanently banned
- banExpiration >  0 => The cook is banned up to the time banExpiration represents, as a unix timestamp in milliseconds


### Complaint ###
Signature: `final class Complaint implements IRepositoryEntity`

Description: Stores complaint data

---

## Storage Classes ##

### IRepository ###
Signature: `interface IRepository`

Description: Generalization of operations on persistent storage with tables of JSON-like values.

Responsabilities:
- Create, Read, Update, Delete, List, and Query Entities from persistent storage.

Methods: (**All of these functions also take** `Class<T> cls` **as their first parameter**)
- `boolean hasId(String id)` (Returns true if an entity with this ID exists, false otherwise)
- `<T extends Entity> T getById(String id)` (Returns an entity with the specified id if it exists, null otherwise)
- `<T extends Entity> void set(T entity)` (Create or overwrite an entity)
- `<T extends Entity> void update(String id, Map<String, Object> properties)` (Updates the properties of the specified entity if it exists)
- `<T extends Entity> void delete(T entity)` (Deletes an entity if it exists)
- `<T extends Entity> List<T> list()` (Returns all elements of a specified table)
- `<T extends Entity> List<T> query(Predicate<T> predicate)` (Returns all elements whose items return true when passed through the given predicate.)

### CloudFirestoreRepository ###
Signature: `class CloudFirestoreRepository implements IRepository`

Description: Implements an IRepository where persistent storage is stored on a Cloud Firestore database.

Responsabilities:
- Maintain a connection to a Cloud Firestore database

---

## Controllers and Views ##

Controllers are implemented as Activities, and View are their layouts.
All Activities are responsible for validating user input *before* sending state-modifying requests to any Model class.

### LoginActivity ###
Description: The main activity. Allows the user log in if no user is current logged in.
If someone else is logged in, this activity should start the appropriate user page.

Responsabilities:
- If the system has already cached a user, immediatly start the appropriate user page
- Otherwise, allow the user to log in and redirect them
- Otherwise, redirect the user to either a Client registration page or a Cook registration page

### LoginPage ###
Description: Allows the user to log in. Changes the current user by calling MealerSystem

Notes: Should tell MealerSystem to login the user, then finish this Activity so that LoginActivity sends them to the appropriate page.

### ClientRegister ###
Description: Allows the user to sign up as a client.

Responsabilities:
- Allow the user to register a new client account with a unique email, then redirect them to the Client page.

Notes: Should tell the System to login the new client, then finish this Activity so that LoginActivity sends them to the Client page.

### CookRegister ###
Description: Allows the user to sign up as a cook, then redirects them to the Cook page.
Responsabilities:
- Allow the user to register a new cook account with a unique email, then redirect them to the Cook page.
- "Accept" an image (show an image prompt but keeps default image)

Notes: Should tell the System to login the new cook, then finish this Activity so that LoginActivity sends them to the Cook page.

### ImageCheck ###
Description: Allows the user to select an image

Responsabilities:
- Open an image selection prompt
- Return the image file (the result is ignored due to file permissions

### AdminHome ###
Description: The Activity that is started when an administrator logs in.

Responsablities:
- Display an inbox of complaints
	- Each item displays the complaint's title, the complaining user, and the reported cook.
	- Clicking on one of the complaint summaries redirects the user to ComplaintActivity.
- Allow the user to log off, sending them back to LoginActivity

### ComplaintActivity ###
Description: The Activity that displays details about a Complaint.

Responsabilities:
- Display the title, concerned users and description of the complaint.
- Marks a complaint as archived when it has been handled.
- Allows the administrator to handle the Complaint.
	- The Suspend button redirect the administrator to the SuspensionActivity.
	- The Dismiss button archives the complaint.
	- The Cancel button finishes the activity.
	
### SuspensionActivity ###
Description: The Activity that allows an administrator to ban/suspend a Cook.

Responsabilities:
- Allows the administrator to specify a ban reason.
- Allows the administrator to suspend the cook
	- Either the administrator permanently suspends the cook
	- Or the administrator specifies a date of expiration to the ban and temporarily suspends the cook

### SuspendedHome ###
Description: The Activity that is started when a banned cook logs in.

Responsabilities:
- Display "Your account has been suspended"
- Display when the ban expires
	- If the ban is permanent, "You will no longer be able to use this account on Mealer"
	- Otherwise, "Your suspension will end on (date) at midnight UTC"
- Display the ban reason

### ClientHome ###
Description: The Activity that is started when a client logs in.

Responsablities:
- Display the message "Welcome, Client"
- Allow the user to log off, sending them back to LoginActivity

### CookHome ###
Description: The Activity that is started when a cook logs in.

Responsablities:
- Display the message "Welcome, Cook"
- Allow the user to log off, sending them back to LoginActivity
