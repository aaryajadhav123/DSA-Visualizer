import { useState } from "react";

function App() {

    const [code, setCode] = useState("");
    const [response, setResponse] = useState("");

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

        const data = await res.text();

        setResponse(data);
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

            <h3>{response}</h3>

        </div>
    );
}

export default App;