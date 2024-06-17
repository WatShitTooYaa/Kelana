const apiUrl = 'http://localhost:3000';
let lastDocId = null;

async function register() {
    const email = document.getElementById('registerEmail').value;
    const password = document.getElementById('registerPassword').value;
    const username = document.getElementById('registerUsername').value;
    const resultDiv = document.getElementById('registerResult');

    try {
        const response = await fetch(`${apiUrl}/api/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password, username }),
        });
        const result = await response.json();
        resultDiv.innerHTML = `<pre>${JSON.stringify(result, null, 2)}</pre>`;
    } catch (error) {
        resultDiv.innerHTML = `<p>Error: ${error.message}</p>`;
    }
}

async function login() {
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;
    const resultDiv = document.getElementById('loginResult');

    try {
        const response = await fetch(`${apiUrl}/api/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password }),
        });
        const result = await response.json();
        if (response.ok) {
            resultDiv.innerHTML = `<p>Login successful: ${result.message}</p>`;
        } else {
            resultDiv.innerHTML = `<p>Error: ${result.message}</p>`;
        }
    } catch (error) {
        resultDiv.innerHTML = `<p>Error: ${error.message}</p>`;
    }
}

async function getRecommendations() {
    const category = document.getElementById('category').value;
    const resultDiv = document.getElementById('recommendationsResult');

    try {
        let url = `${apiUrl}/api/recommendations?category=${category}&limit=10`;
        if (lastDocId) {
            url += `&startAfter=${lastDocId}`;
        }

        const response = await fetch(url);
        const result = await response.json();
        if (response.ok) {
            lastDocId = result[result.length - 1]?.id || null; // Store the last document ID
            resultDiv.innerHTML = `<pre>${JSON.stringify(result, null, 2)}</pre>`;
        } else {
            resultDiv.innerHTML = `<p>Error: ${result.message}</p>`;
        }
    } catch (error) {
        resultDiv.innerHTML = `<p>Error: ${error.message}</p>`;
    }
}
