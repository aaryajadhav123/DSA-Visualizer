import { useState, useEffect } from "react";

function App() {

    const [code, setCode] = useState("");
    const [steps, setSteps] = useState([]);
    const [currentStep, setCurrentStep] = useState(0);
    const [isPlaying, setIsPlaying] = useState(false);
    const [darkMode, setDarkMode] = useState(false);

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
        setCurrentStep(0);
    };
    const stepObj = steps[currentStep];

    const previousStep =
    currentStep > 0
        ? steps[currentStep - 1]
        : null;

        useEffect(() => {

            if (!isPlaying) return;
        
            const timer = setInterval(() => {
        
                setCurrentStep(prev => {
        
                    if (prev >= steps.length - 1) {
                        setIsPlaying(false);
                        return prev;
                    }
        
                    return prev + 1;
                });
        
            }, 1000);
        
            return () => clearInterval(timer);
        
        }, [isPlaying, steps]);

        return (
            <div
                style={{
                    padding: "20px",
                    display: "flex",
                    gap: "30px",
                    alignItems: "flex-start",

                    backgroundColor: darkMode
                        ? "#111827"
                        : "#ffffff",

                    color: darkMode
                        ? "#ffffff"
                        : "#000000",

                    minHeight: "100vh"
                }}
>
            <div style={{ flex: 1 }}>

            <h1>AI DSA Visualizer</h1>

            <textarea
                value={code}
                onChange={(e) => setCode(e.target.value)}
                style={{
                    width: "100%",
                    height: "300px",
                    fontFamily: "monospace",
                    fontSize: "14px",

                    backgroundColor: darkMode
                        ? "#1f2937"
                        : "#ffffff",

                    color: darkMode
                        ? "#ffffff"
                        : "#000000"
                }}
            />

            <br />
            <br />

            <button onClick={handleVisualize}>
                Visualize
            </button>

            <button
                onClick={() => setDarkMode(!darkMode)}
                style={{ marginLeft: "10px" }}
            >
                {darkMode ? "Light Mode" : "Dark Mode"}
            </button>

            <br />
<br />



</div>

<div style={{ flex: 1 }}>
{steps.length > 0 && (
    <div>
        <button
            onClick={() => setCurrentStep(currentStep - 1)}
            disabled={currentStep === 0}
        >
            Previous
        </button>

        <span style={{ margin: "0 15px" }}>
            Step {currentStep + 1} of {steps.length}
        </span>

        <button
            onClick={() => setCurrentStep(currentStep + 1)}
            disabled={currentStep === steps.length - 1}
        >
            Next
        </button>

        <button
            onClick={() => setIsPlaying(!isPlaying)}
            style={{ marginLeft: "15px" }}
        >
            {isPlaying ? "Pause" : "Play"}
        </button>
    </div>
)}

            {steps.length > 0 && (
    <div
        style={{
            border: "1px solid #ddd",
            backgroundColor: darkMode
            ? "#1f2937"
            : "#ffffff",
            borderRadius: "10px",
            padding: "15px",
            marginTop: "15px",
            boxShadow: "0 2px 5px rgba(0,0,0,0.1)"
        }}
    >
       

       {Object.entries(stepObj.variables).map(([name, value]) => {

                const variableChanged =
                    previousStep &&
                    previousStep.variables[name] !== value;

                return (
                    <p
                        key={name}
                        style={{
                            backgroundColor:
                                variableChanged
                                    ? "#fef08a"
                                    : "transparent",
                            padding: "4px"
                        }}
                    >
                        {name} = {value}
                    </p>
                );
                })}

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
                {values.map((value, index) => {

                        const isChanged =
                            previousStep &&
                            previousStep.arrays &&
                            previousStep.arrays[arrayName] &&
                            previousStep.arrays[arrayName][index] !== value;

                        return (
                            <div
                                key={index}
                                style={{
                                    border: "2px solid #2563eb",
                                    borderRadius: "8px",
                                    padding: "12px 0",
                                    minWidth: "60px",
                                    textAlign: "center",
                                    fontWeight: "bold",
                                    backgroundColor: isChanged
                                        ? "#fef08a"
                                        : darkMode
                                        ? "#374151"
                                        : "#eff6ff",
                                    color: isChanged
                                        ? "#000000"                            
                                        : darkMode
                                        ? "#ffffff"                              
                                        : "#000000"
                                }}
                            >
                                {value}
                            </div>
                        );
                        })}
                </div>
            </div>
        ))}
    </div>
)}
</div>

        </div>
    );
}

export default App;