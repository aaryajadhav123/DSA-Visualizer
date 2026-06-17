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
                border: "1px solid black",
                padding: "10px",
                marginTop: "10px"
            }}
        >
            <h3>Step {stepObj.step}</h3>

            {Object.entries(stepObj.variables).map(([name, value]) => (
                <p key={name}>
                    {name} = {value}
                </p>
            ))}
        </div>
    ))}
</div>

        </div>
    );
}

export default App;