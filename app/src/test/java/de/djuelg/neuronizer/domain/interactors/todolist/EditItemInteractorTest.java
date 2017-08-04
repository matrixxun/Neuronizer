package de.djuelg.neuronizer.domain.interactors.todolist;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.InputMismatchException;

import de.djuelg.neuronizer.domain.executor.Executor;
import de.djuelg.neuronizer.domain.executor.MainThread;
import de.djuelg.neuronizer.domain.interactors.todolist.impl.EditItemInteractorImpl;
import de.djuelg.neuronizer.domain.model.todolist.TodoListItem;
import de.djuelg.neuronizer.domain.repository.TodoListRepository;
import de.djuelg.neuronizer.storage.TodoListRepositoryMock;
import de.djuelg.neuronizer.threading.TestMainThread;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;

/**
 * Created by djuelg on 10.07.17.
 */
public class EditItemInteractorTest {

    private MainThread mainThread;
    private TodoListRepository repository;
    @Mock private Executor executor;
    @Mock private EditItemInteractor.Callback mockedCallback;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mainThread = new TestMainThread();
        repository = new TodoListRepositoryMock();
    }

    @Test
    public void testInsertViaUpdate() throws Exception {
        EditItemInteractorImpl interactor = new EditItemInteractorImpl(executor, mainThread, mockedCallback, repository, "uuid", "title", 0 , false, "" , false, "header-id");
        interactor.run();

        TodoListRepositoryMock repositoryMock = (TodoListRepositoryMock) repository;
        assertEquals(repositoryMock.updateCount, 1);
        assertEquals(repositoryMock.uuids.size(), 1);
        Mockito.verify(mockedCallback).onItemUpdated(any(TodoListItem.class));
    }

    @Test
    public void testRealUpdate() throws Exception {
        EditItemInteractorImpl interactor = new EditItemInteractorImpl(executor, mainThread, mockedCallback, repository, "uuid", "title", 0 , false, "" , false, "header-id");
        interactor.run();
        interactor.run();

        TodoListRepositoryMock repositoryMock = (TodoListRepositoryMock) repository;
        assertEquals(2, repositoryMock.updateCount);
        assertEquals(1, repositoryMock.uuids.size());
        Mockito.verify(mockedCallback, Mockito.atLeastOnce()).onItemUpdated(any(TodoListItem.class));
    }

    @Test(expected = InputMismatchException.class)
    public void testParentNotExisting() throws Exception {
        EditItemInteractorImpl interactor = new EditItemInteractorImpl(executor, mainThread, mockedCallback, repository, "uuid", "title", 0, false, "" , false, "MISSING_UUID");
        interactor.run();

        TodoListRepositoryMock repositoryMock = (TodoListRepositoryMock) repository;
        assertEquals(0, repositoryMock.insertCount);
        assertEquals(0, repositoryMock.uuids.size());
    }
}