import { useState } from "react";

function App() {

    const [code, setCode] = useState("");
    const [steps, setSteps] = useState([]);

    const handleVisualize = async () => {

        const res = await fetch(
            "http://localhost:8080/api/visualize",
            {
                method: "POST",
                headers: {
                    "Content-Type": "text/plain"
                },
                body: code
            }
        );

        const data = await res.json();

        setSteps(data);
    };

    return (
        <div style={{ padding: "20px" }}>

            <h1>AI DSA Visualizer</h1>

            <textarea
                rows="10"
                cols="60"
                value={code}
                onChange={(e) => setCode(e.target.value)}
            />

            <br />
            <br />

            <button onClick={handleVisualize}>
                Visualize
            </button>

            <div>
    {steps.map((stepObj) => (
        <div
            key={stepObj.step}
            style={{
                border: "1px solid #ddd",
                borderRadius: "10px",
                padding: "15px",
                marginTop: "15px",
                boxShadow: "0 2px 5px rgba(0,0,0,0.1)"
            }}
        >
            <h3>Step {stepObj.step}</h3>

            {Object.entries(stepObj.variables).map(([name, value]) => (
                <p key={name}>
                    {name} = {value}
                </p>
                
            ))}

{Object.entries(stepObj.arrays || {}).map(([arrayName, values]) => (
    <div key={arrayName}>
        <p>{arrayName}</p>

        <div
    style={{
        display: "flex",
        gap: "5px",
        marginBottom: "5px"
    }}
>
    {values.map((_, index) => (
        <div
            key={index}
            style={{
                minWidth: "60px",
                textAlign: "center"
            }}
        >
            {index}
        </div>
    ))}
</div>

        <div
            style={{
                display: "flex",
                gap: "5px"
            }}
        >
            {values.map((value, index) => (
                <div
                    key={index}
                    style={{
                        border: "2px solid #2563eb",
                        borderRadius: "8px",
                        padding: "12px 0",
                        minWidth: "60px",
                        textAlign: "center",
                        fontWeight: "bold",
                        backgroundColor: "#eff6ff"
                    }}
                >
                    {value}
                </div>
            ))}
        </div>
    </div>
))}
        </div>
    ))}
</div>

        </div>
    );
}

export default App;