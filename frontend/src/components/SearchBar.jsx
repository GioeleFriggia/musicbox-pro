import { useState } from 'react'

export default function SearchBar({ onSearch, loading }) {
  const [query, setQuery] = useState('lofi')

  const submit = (e) => {
    e.preventDefault()
    onSearch(query)
  }

  return (
    <form className="search-bar" onSubmit={submit}>
      <input
        type="text"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        placeholder="Cerca artisti, album, mood, genere..."
      />
      <button disabled={loading}>{loading ? 'Ricerca...' : 'Cerca'}</button>
    </form>
  )
}
