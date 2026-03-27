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
async function login() {

    alert("Login not implemented yet 😄");

}