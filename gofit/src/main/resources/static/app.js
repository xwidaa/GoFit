function showLogin() {
    document.getElementById("loginBox").style.display = "block";
    document.getElementById("registerBox").style.display = "none";
}

function showRegister() {
    document.getElementById("loginBox").style.display = "none";
    document.getElementById("registerBox").style.display = "block";
}

// REGISTER
async function register() {

    const data = {
        email: document.getElementById("regEmail").value,
        password: document.getElementById("regPassword").value,


        height: parseFloat(document.getElementById("regHeight").value),
        weight: parseFloat(document.getElementById("regWeight").value),

        // temporar:
        birthDate: "2000-01-01",
        gender: "MALE",
        targetWeight: 70,
        activityLevel: "MODERATE",
        goal: "MAINTAIN",
        experienceLevel: "BEGINNER"
    };

    console.log(data);

    const response = await fetch("http://localhost:8083/auth/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    });

    if (response.ok) {
        alert("User created!");
    } else {
        alert("Error!");
    }
}

// LOGIN (placeholder momentan)
// LOGIN (Implemented 🚀)
async function login() {
    const email = document.getElementById("email").value; // Matches your UI ID
    const password = document.getElementById("password").value; // Matches your UI ID

    const data = {
        email: email,
        password: password
    };

    try {
        const response = await fetch("http://localhost:8083/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            const result = await response.json();

            // 1. Save the token from the backend Map
            localStorage.setItem("token", result.token);

            // 2. Redirect to your user dashboard
            window.location.href = "overview.html";
        } else {
            const errorText = await response.text();
            alert("Login failed: " + errorText);
        }
    } catch (error) {
        console.error("Connection Error:", error);
        alert("Could not connect to the server. Check if Spring Boot is running on port 8083.");
    }
}