package de.djuelg.neuronizer.storage;

import de.djuelg.neuronizer.domain.model.TodoList;
import de.djuelg.neuronizer.domain.model.TodoListHeader;
import de.djuelg.neuronizer.domain.model.TodoListItem;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.storage.converter.RealmConverter;
import de.djuelg.neuronizer.storage.model.TodoListDAO;
import de.djuelg.neuronizer.storage.model.TodoListHeaderDAO;
import de.djuelg.neuronizer.storage.model.TodoListItemDAO;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by dmilicic on 1/29/16.
 */
public class TodoListRepositoryImpl implements TodoListRepository {

    private final RealmConfiguration configuration;

    public TodoListRepositoryImpl() {
        this(Realm.getDefaultConfiguration());
    }

    // RealmConfiguration injectable for testing
    TodoListRepositoryImpl(RealmConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public TodoList getTodoListById(String uuid) {
        Realm realm = Realm.getInstance(configuration);
        TodoListDAO todoListDAO = realm.where(TodoListDAO.class).equalTo("uuid", uuid).findFirst();
        TodoList todoList = (todoListDAO != null)
                ? RealmConverter.convert(todoListDAO)
                : null;
        realm.close();
        return todoList;
    }

    @Override
    public TodoListHeader getHeaderById(String uuid) {
        Realm realm = Realm.getInstance(configuration);
        TodoListHeaderDAO headerDAO = realm.where(TodoListHeaderDAO.class).equalTo("uuid", uuid).findFirst();
        TodoListHeader header = (headerDAO != null)
                ? RealmConverter.convert(headerDAO)
                : null;
        realm.close();
        return header;
    }

    @Override
    public TodoListItem getItemById(String uuid) {
        Realm realm = Realm.getInstance(configuration);
        TodoListItemDAO itemDAO = realm.where(TodoListItemDAO.class).equalTo("uuid", uuid).findFirst();
        TodoListItem item = (itemDAO != null)
                ? RealmConverter.convert(itemDAO)
                : null;
        realm.close();
        return item;
    }

    @Override
    public boolean insert(TodoListHeader header) {
        Realm realm = Realm.getInstance(configuration);
        final TodoListHeaderDAO dao = RealmConverter.convert(header);

        realm.beginTransaction();
        try {
            realm.copyToRealm(dao);
            realm.commitTransaction();
        } catch (Throwable throwable) {
            realm.cancelTransaction();
            realm.close();
            return false;
        }
        realm.close();
        return true;
    }

    @Override
    public boolean insert(TodoListItem item) {
        Realm realm = Realm.getInstance(configuration);
        final TodoListItemDAO dao = RealmConverter.convert(item);

        realm.beginTransaction();
        try {
            realm.copyToRealm(dao);
            realm.commitTransaction();
        } catch (Throwable throwable) {
            realm.cancelTransaction();
            realm.close();
            return false;
        }
        realm.close();
        return true;
    }

    @Override
    public void delete(final TodoListHeader deletedItem) {
        Realm realm = Realm.getInstance(configuration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TodoListHeaderDAO dao = realm.where(TodoListHeaderDAO.class).equalTo("uuid", deletedItem.getUuid()).findFirst();
                if (dao != null) dao.deleteFromRealm();
            }
        });
        realm.close();
    }

    @Override
    public void delete(final TodoListItem deletedItem) {
        Realm realm = Realm.getInstance(configuration);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TodoListItemDAO dao = realm.where(TodoListItemDAO.class).equalTo("uuid", deletedItem.getUuid()).findFirst();
                if (dao != null) dao.deleteFromRealm();
            }
        });
        realm.close();
    }

    @Override
    public void update(TodoListHeader updatedItem) {
        Realm realm = Realm.getInstance(configuration);
        final TodoListHeaderDAO dao = RealmConverter.convert(updatedItem);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(dao);
            }
        });
        realm.close();
    }

    @Override
    public void update(TodoListItem updatedItem) {
        Realm realm = Realm.getInstance(configuration);
        final TodoListItemDAO dao = RealmConverter.convert(updatedItem);
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(dao);
            }
        });
        realm.close();
    }
}