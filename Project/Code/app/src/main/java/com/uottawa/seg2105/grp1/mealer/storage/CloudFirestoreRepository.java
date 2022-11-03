package com.uottawa.seg2105.grp1.mealer.storage;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.uottawa.seg2105.grp1.mealer.model.IRepositoryEntity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.uottawa.seg2105.grp1.mealer.model.EntityDeserialisationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

/**
 * Repository implementation use Cloud Firestore
 */
public final class CloudFirestoreRepository implements IRepository {

    private static final String TAG = "Cloud Firestore Repository";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public <T extends IRepositoryEntity> boolean hasId(Class<T> cls, String id) throws RepositoryRequestException {
        try {
            // Block until the DocumentSnapshot can be retrieved
            Task<DocumentSnapshot> task_docsnap = db.collection(IRepositoryEntity.getTableName(cls)).document(id).get();
            DocumentSnapshot docsnap = Tasks.await(task_docsnap);

            // Return whether or not the Entity exists
            Log.d(TAG, cls.getSimpleName() + " hasId: Successfully fetched document with id " + id);
            return docsnap.exists();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            Log.e(TAG, cls.getSimpleName() + " hasId: id " + id, e);
            throw new RepositoryRequestException();
        }
    }

    @Override
    public <T extends IRepositoryEntity> T getById(Class<T> cls, String id) throws RepositoryRequestException {
        try {
            // Block until the DocumentSnapshot can be retrieved
            Task<DocumentSnapshot> task_docsnap = db.collection(IRepositoryEntity.getTableName(cls)).document(id).get();
            DocumentSnapshot docsnap = Tasks.await(task_docsnap);

            // Try to convert the document's data to the Entity's type
            if (!docsnap.exists())
                return null;
            T entity = cls.newInstance();
            entity.deserialise(docsnap.getData());

            Log.d(TAG, cls.getSimpleName() + " getById: Successfully deserialised document with id " + id);
            return entity;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            Log.e(TAG, cls.getSimpleName() + " getById: id " + id, e);
            throw new RepositoryRequestException();
        }
    }

    @Override
    public <T extends IRepositoryEntity> void set(Class<T> cls, T entity) throws RepositoryRequestException {
        try {
            // Attempt to serialise the Entity
            Map<String, Object> serialised = entity.serialise();

            // Block until the document has been set
            Task<Void> task_setdoc = db.collection(IRepositoryEntity.getTableName(cls)).document(entity.getId()).set(serialised);
            Tasks.await(task_setdoc);

            Log.d(TAG, cls.getSimpleName() + " set: Successfully created/overwrote document with id " + entity.getId());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            Log.e(TAG, cls.getSimpleName() + " set: id " + entity.getId(), e);
            throw new RepositoryRequestException();
        }
    }

    @Override
    public <T extends IRepositoryEntity> void update(Class<T> cls, String id, Map<String, Object> properties) throws RepositoryRequestException {
        try {
            // Block until the file is updated
            Task<Void> task_update = db.collection(IRepositoryEntity.getTableName(cls)).document(id).update(properties);
            Tasks.await(task_update);

            Log.d(TAG, cls.getSimpleName() + " update: Successfully updated document with id " + id);
        } catch (RuntimeException e) {
            throw e;
        } catch (ExecutionException e) {
            Log.d(TAG, cls.getSimpleName() + " update: No document with id " + id + " to update");
        } catch (Exception e) {
            Log.e(TAG, cls.getSimpleName() + " update: id " + id, e);
            throw new RepositoryRequestException();
        }
    }

    @Override
    public <T extends IRepositoryEntity> void delete(Class<T> cls, T entity) throws RepositoryRequestException {
        try {
            // Block until the file is deleted
            Task<Void> task_delete = db.collection(IRepositoryEntity.getTableName(cls)).document(entity.getId()).delete();
            Tasks.await(task_delete);

            Log.d(TAG, cls.getSimpleName() + " delete: Successfully deleted document with id " + entity.getId());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            Log.e(TAG, cls.getSimpleName() + " delete: id " + entity.getId(), e);
            throw new RepositoryRequestException();
        }
    }

    @Override
    public <T extends IRepositoryEntity> List<T> list(Class<T> cls) throws RepositoryRequestException {
        try {
            // Block until all elements have been returned
            Task<QuerySnapshot> task_querysnap = db.collection(IRepositoryEntity.getTableName(cls)).get();
            QuerySnapshot querysnap = Tasks.await(task_querysnap);

            // Iterate over all elements
            List<T> entities = new ArrayList<>(querysnap.size());
            for (QueryDocumentSnapshot qdocsnap : querysnap) {
                // Try to deserialise the documents and add them to the list
                try {
                    T entity = cls.newInstance();
                    entity.deserialise(qdocsnap.getData());
                    entities.add(entity);

                } catch (EntityDeserialisationException e) {
                    // Ignore invalid documents
                    Log.d(TAG, " list: Ignored invalid document with id " + qdocsnap.getId(), e);
                }
            }

            return entities;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            Log.e(TAG, cls.getSimpleName() + " list: ", e);
            throw new RepositoryRequestException();
        }
    }

    @Override
    public <T extends IRepositoryEntity> List<T> query(Class<T> cls, Predicate<T> predicate) throws RepositoryRequestException {
        try {
            // Block until all elements have been returned
            Task<QuerySnapshot> task_querysnap = db.collection(IRepositoryEntity.getTableName(cls)).get();
            QuerySnapshot querysnap = Tasks.await(task_querysnap);

            // Iterate over all elements
            List<T> entities = new ArrayList<>(querysnap.size());
            for (QueryDocumentSnapshot qdocsnap : querysnap) {
                // Try to deserialise the documents and add them to the list
                try {
                    T entity = cls.newInstance();
                    entity.deserialise(qdocsnap.getData());

                    // Only add entities that meet the predicate's conditions
                    if (predicate.test(entity))
                        entities.add(entity);

                } catch (EntityDeserialisationException e) {
                    // Ignore invalid documents
                    Log.d(TAG, " list: Ignored invalid document with id " + qdocsnap.getId(), e);
                }
            }

            return entities;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            Log.e(TAG, cls.getSimpleName() + " list: ", e);
            throw new RepositoryRequestException();
        }
    }

    @Override
    public <T extends IRepositoryEntity> String getAutoID(Class<T> cls) throws RepositoryRequestException {
        try {
            return db.collection(IRepositoryEntity.getTableName(cls)).document().getId();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            Log.e(TAG, cls.getSimpleName() + " getAutoID: ", e);
            throw new RepositoryRequestException();
        }
    }
}
