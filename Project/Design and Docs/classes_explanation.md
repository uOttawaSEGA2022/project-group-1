# Classes and their Responsabilities #

A description of classes and their roles.

## Model Classes ##

Model classes should be able to safely block.
As such, the Activities must communicate with them asynchronously.

### System ###
Signature: `final class System extends Application`

Description: Single point of access for many resources 

Responsabilities:
- Store static variables such as the current user's session data, the current IRepository implementation instance, etc.

Responsabilities:
- Provide initialization of the application state
- Provide access to global variables
- Log in and log out (change current user)

Static variables:
- `User currentUser`
- `IRepository repository`

Methods:
- `void onCreate()`
- `boolean trylogin(String username, String password)`
- `private login(User user)`
- `void logoff()`
- `User getCurrentUser()`
- `IRepository getRepository()`

### Entity ###
Signature: `abstract class Entity`

Description: Generalization of a Domain Model Entity that can be stored to and retrieved from permanent storage.

**All subclasses of Entity should provide an empty constructor callable by IRepository**

Responsabilities:
- Provide serialisation to a JSON-compatible Map
- Provide deserialisation from a JSON-compatible Map
- Provide a table name and id to identify the object in permanent storage.

Static variables:
- `Map<Class<? extends Entity>, String> tableNames` (provides table names for each subclass of Entity. **MUST BE UPDATED MANUALLY**)

Methods:
- `abstract String getId()`
- `abstract Map<Object, String> serialise()` (Called on an object to get a JSON-Compatible Map)
- `abstract void deserialise(Map<Object, String> map)` (Called on an empty constructor to initialise attributes.)

### EntityFragment ###
Signature: `abstract class EntityFragment extends Entity`

Description: Specialization of Entity which is stored within another Entity instead of in a storage table. 
getId() returns null.

### User ###
Signature: `class User extends Entity`

Description: Stores user data common to all user types.

Responsabilities:
- Establish a connection to its UserRole

Variables:
- `String firstName, lastName, email, address`
- `String passwordHash` (*Password is hashed for at least a minimum of security*)
- `UserRole role`

Methods:
- getters
- `static User getByEmail(String email)`

### UserRole ###
Sginature: `abstract class UserRole`

Description: Generalization of Roles a User account can possess.

Responsabilities: TBD

Variables:
- `User user`

Methods:
- getter

### ClientRole ###
Signature: `class ClientRole extends UserRole`

Description: Stores user data for clients.

Variables:
- `String cardNumber`

Methods:
- getter, setter

### CookRole ###
Signature: `class CookRole extends UserRole`

Description: Stores user data for cooks.

Variables:
- `String description`

Methods:
- getter, setter

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

Responsabilities: TBD

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

### ClientRegisterActivity ###
Description: Allows the user to sign up as a client, then redirects them to the Client page.

Responsabilities:
- Allow the user to register a new client account with a unique email, then redirect them to the Client page.

Notes: Should tell the System to login the new client, then finish this Activity so that LoginActivity sends them to the Client page.

### CookRegisterActivity ###
Description: Allows the user to sign up as a cook, then redirects them to the Cook page.
Responsabilities:
- Allow the user to register a new cook account with a unique email, then redirect them to the Cook page.

Notes: Should tell the System to login the new cook, then finish this Activity so that LoginActivity sends them to the Cook page.

### AdministratorPage ###
Description: The Activity that is started when an administrator logs in.

Responsablities:
- Display the message "Welcome, Administrator"
- Allow the user to log off, sending them back to LoginActivity

### ClientPage ###
Description: The Activity that is started when a client logs in.

Responsablities:
- Display the message "Welcome, Client"
- Allow the user to log off, sending them back to LoginActivity

### CookPage ###
Description: The Activity that is started when a cook logs in.

Responsablities:
- Display the message "Welcome, Cook"
- Allow the user to log off, sending them back to LoginActivity