const apiUrl = 'http://localhost:3000';
let lastDocId = null;
let authToken = null; // Token akan disimpan di sini setelah login

async function register() {
    const email = document.getElementById('registerEmail').value;
    const password = document.getElementById('registerPassword').value;
    const username = document.getElementById('registerUsername').value;
    const resultDiv = document.getElementById('registerResult');

    try {
        const response = await fetch(`${apiUrl}/auth/register`, {
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
        const response = await fetch(`${apiUrl}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password }),
        });
        const result = await response.json();
        if (response.ok) {
            authToken = result.token; // Simpan token
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
        let url = `${apiUrl}/recommendations?category=${category}&limit=10`;
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

async function getCategories() {
    const resultDiv = document.getElementById('categoriesResult');

    try {
        const response = await fetch(`${apiUrl}/category`);
        const result = await response.json();
        if (response.ok) {
            resultDiv.innerHTML = `<pre>${JSON.stringify(result, null, 2)}</pre>`;
        } else {
            resultDiv.innerHTML = `<p>Error: ${result.message}</p>`;
        }
    } catch (error) {
        resultDiv.innerHTML = `<p>Error: ${error.message}</p>`;
    }
}

async function getTripPlan() {
    const city = document.getElementById('city').value;
    const priceThreshold = document.getElementById('priceThreshold').value;
    const destinationLimitPerDay = document.getElementById('destinationLimitPerDay').value;
    const numDays = document.getElementById('numDays').value;
    const resultDiv = document.getElementById('tripPlanResult');

    try {
        const response = await fetch(`${apiUrl}/tripplanner?city=${city}&priceThreshold=${priceThreshold}&destinationLimitPerDay=${destinationLimitPerDay}&numDays=${numDays}`);
        const text = await response.text(); // Ambil respons sebagai teks mentah
        console.log(text); // Log respons mentah untuk debugging
        const result = JSON.parse(text); // Parse teks mentah sebagai JSON
        if (response.ok) {
            resultDiv.innerHTML = `<pre>${JSON.stringify(result, null, 2)}</pre>`;
        } else {
            resultDiv.innerHTML = `<p>Error: ${result.message}</p>`;
        }
    } catch (error) {
        console.error('Error fetching trip plan:', error); // Log error untuk debugging
        resultDiv.innerHTML = `<p>Error: ${error.message}</p>`;
    }
}

async function likeTourism() {
    const placeId = document.getElementById('placeId').value;
    const resultDiv = document.getElementById('likeResult');

    try {
        const response = await fetch(`${apiUrl}/likes/like`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            },
            body: JSON.stringify({ place_id: placeId }),
        });
        const result = await response.json();
        if (response.ok) {
            resultDiv.innerHTML = `<p>Tourism liked successfully: ${JSON.stringify(result, null, 2)}</p>`;
        } else {
            resultDiv.innerHTML = `<p>Error: ${result.message}</p>`;
        }
    } catch (error) {
        resultDiv.innerHTML = `<p>Error: ${error.message}</p>`;
    }
}

async function getLikedTourism() {
    const resultDiv = document.getElementById('likedTourismResult');

    try {
        const response = await fetch(`${apiUrl}/likes/likedTourism`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${authToken}`
            },
        });
        const result = await response.json();
        console.log(result); // Log the result for debugging
        if (response.ok) {
            resultDiv.innerHTML = `<pre>${JSON.stringify(result, null, 2)}</pre>`;
        } else {
            resultDiv.innerHTML = `<p>Error: ${result.message}</p>`;
        }
    } catch (error) {
        console.error('Error fetching liked tourism:', error); // Log error for debugging
        resultDiv.innerHTML = `<p>Error: ${error.message}</p>`;
    }
}
