# Basic Model #
This app will generally follow the MVC+Repository approach.

## Model ##
The Model classes implement logic (the system) and data (entity/domain model classes) in the application

Along with storing all the data they require, they must also implement their responsabilities as public functions that Controller classes can call.

Models that need to be stored in the database are Entities. These classes must provide functions for serialisation to and deserialisation from JSON-like maps (unlike the Serializable interface)

All Entity classes with a corresponding table in persistent storage should also provide a method getId() that the Repository implementation can use to search up its data.

An Entity can also contain EntityFragments who can also be serialised and deserialised but do not have an ID.

**Entities are expected to call Repository on themselves when necessary**

The `getId()` function should return a string ID unique to this instance.

The `serialise()` function should return a `Map<String, Object>` object with all of the `instance`'s fields.

The `deserialise(Map<String, Object> map)` function, when 

## View ##
The Views are simply the XML layouts of Activity objects.

They display data passed to them by their corresponding Controller and make requests to the latter.

## Controller ##
The Controller classes are Activities in charge of validating and responding to user events.

They also update their UI when changes in the Model occur.

Since model classes are allowed to block, Controller classes should call them asynchronously

## Repository ##
An abstracted storage interface that declares Create, Read, Update, Delete, List and Query methods for Entities.

Its implementation will use the getId(), serialise(), and deserialise() methods of Entities to store and retrieve data.