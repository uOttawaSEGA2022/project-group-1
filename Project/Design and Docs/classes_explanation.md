# Classes and their Responsabilities #

A description of classes and their roles.

## Model Classes ##

### System ###
Signature: `abstract final class System`

Description: Single point of access for many resources 

Responsabilities:
- Store static variables such as the current user's session data, the current IRepository implementation instance, etc.

Responsabilities:
- Provide initialization of the application state
- Provide access to global variables
- Log in and log out (change current user)

Static variables:
- `static boolean init`
- `static User currentUser`
- `static IRepository repository`

Methods:
- `static void initiate()` (Should create the repository instance)
- `static void stop()` (Should reset global variables)
- `static boolean hasInit()`
- `static void login(User user)`
- `static void logoff()`
- `static User getCurrentUser()`
- `static IRepository getRepository()`

### Entity ###
Signature: `abstract class Entity<T,I>`

Description: Generalization of a Domain Model Entity that can be stored to and retrieved from permanent storage.

Responsabilities:
- Provide serialisation to a JSON-compatible Map
- Provide deserialisation from a JSON-compatible Map
- Provide a table name and id to identify the object in permanent storage.

Static variables:
- `Map<Class extends Entity, String> tableNames` (provides table names for each subclass of IEntity. **MUST BE UPDATED MANUALLY**)

Methods:
- `I getId()` (returns null by default)
- `abstract Map<Object, String> serialise(T inst)`
- `abstract T deserialise(Map<Object, String> map)`

### User ###
Signature: `class User implements Entity<User, String>`

Description: Stores user data common to all user types.

Responsabilities: TBD

Variables:
- `String firstName, lastName, email, address`
- `String passwordHash` (*Password is hashed for at least a minimum of security*)
- `UserRole role`

Methods:
- getters

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

## Storage Classes ##

### IRepository ###
Signature: `interface IRepository`

Description: Generalization of operations on persistent storage with tables of JSON-like values.

Responsabilities:
- Create, Read, Update, Delete, List, and Query Entities from persistent storage.

Methods: (Assuming `T extends IEntity<T, I>`)
- `boolean create<T extends IEntity>(T entity)` (Returns true if the entity was added to storage, false otherwise)
- `T getById<T,I>(I id)` (Returns an entity with the specified id if it exists, null otherwise)
- `boolean set<T extends IEntity>(T entity)` (Returns true if the entity was set successfully in storage, false otherwise)
- `boolean delete<T extends IEntity>(T entity)` (Returns true if the entity was removed or if it already didn't exist, false otherwise)
- `List<T> list<T extends IEntity>()` (Returns all elements of a specified table)
- `List<T> query<T extends IEntity>(Predicate<T> predicate)` (Returns all elements whose items return true when passed through the given predicate.)

### CloudFirestoreRepository ###
Signature: `class CloudFirestoreRepository implements IRepository`

Description: Implements an IRepository where persistent storage is stored on a Cloud Firestore database.

Responsabilities: TBD