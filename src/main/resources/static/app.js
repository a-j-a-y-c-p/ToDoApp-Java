document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('todoForm');
    const titleInput = document.getElementById('title');
    const todoList = document.getElementById('todoList');

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const title = titleInput.value;
        await addTodo({ title, completed: false });
        titleInput.value = '';
        loadTodos();
    });

    async function loadTodos() {
        const response = await fetch('/todos');
        const todos = await response.json();
        todoList.innerHTML = '';
        todos.forEach(todo => {
            const tr = document.createElement('tr');
            tr.className = todo.completed ? 'completed' : '';

            // Truncate the title to 250 words
            const truncatedTitle = truncateText(todo.title, 250);

            tr.innerHTML = `
                <td>${truncatedTitle}</td>
                <td>
                    <input type="checkbox" ${todo.completed ? 'checked' : ''} onchange="toggleComplete(${todo.id}, this)">
                    <span>${todo.completed ? 'Completed' : 'Incompleted'}</span>
                </td>
                <td>
                    <button class="delete" onclick="deleteTodo(${todo.id})">Delete</button>
                </td>
            `;
            todoList.appendChild(tr);
        });
    }

    function truncateText(text, wordLimit) {
        const words = text.split(' ');
        if (words.length > wordLimit) {
            return words.slice(0, wordLimit).join(' ') + '...';
        }
        return text;
    }

    async function addTodo(todo) {
        await fetch('/todos', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(todo)
        });
    }

    async function updateTodoStatus(id, completed) {
        await fetch(`/todos/${id}`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ completed })
        });
    }

    window.deleteTodo = async function(id) {
        await fetch(`/todos/${id}`, { method: 'DELETE' });
        loadTodos();
    }

    window.toggleComplete = async function(id, checkbox) {
        const completed = checkbox.checked;
        await updateTodoStatus(id, completed);
        loadTodos();
    }

    loadTodos();
});
