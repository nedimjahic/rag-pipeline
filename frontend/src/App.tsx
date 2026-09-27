import {useState} from 'react'
import {useMutation} from '@tanstack/react-query'
import './App.css'

type Source = { text: string; category: string; score: number }
type Answer = { text: string; sources: Source[] }

function App() {
    const [question, setQuestion] = useState('')
    const [history, setHistory] = useState<{ q: string; a: Answer }[]>([])

    const ask = useMutation({
        mutationFn: async (q: string): Promise<Answer> => {
            const res = await fetch('/api/chat/ask', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({text: q}),
            })
            if (!res.ok) throw new Error(`HTTP ${res.status}`)
            return res.json()
        },
        onSuccess: (a, q) => setHistory(h => [...h, {q, a}]),
    })

    const submit = (e: React.FormEvent) => {
        e.preventDefault()
        if (!question.trim()) return
        ask.mutate(question)
        setQuestion('')
    }

    return (
        <main style={{maxWidth: 700, margin: '2rem auto', fontFamily: 'sans-serif'}}>
            <h1>RAG lab</h1>
            {history.map((item, i) => (
                <div key={i} style={{marginBottom: '1.5rem'}}>
                    <p><strong>Q:</strong> {item.q}</p>
                    <p><strong>A:</strong> {item.a.text}</p>
                    <details>
                        <summary>{item.a.sources.length} sources</summary>
                        <ul>{item.a.sources.map((s, j) =>
                            <li key={j}>[{s.category}] {s.text} <em>({s.score.toFixed(3)})</em></li>
                        )}</ul>
                    </details>
                </div>
            ))}
            <form onSubmit={submit}>
                <input value={question} onChange={e => setQuestion(e.target.value)}
                       placeholder="Ask about your documents…" style={{width: '80%'}}/>
                <button type="submit" disabled={ask.isPending}>
                    {ask.isPending ? '…' : 'Ask'}
                </button>
            </form>
            {ask.isError && <p style={{color: 'red'}}>{String(ask.error)}</p>}
        </main>
    )
}

export default App
