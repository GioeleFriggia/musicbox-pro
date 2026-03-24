import { useState } from 'react'

export default function PlaylistPanel({ playlists, onCreate, onAddTrack, selectedTrack }) {
  const [name, setName] = useState('')
  const [description, setDescription] = useState('')

  const submit = async (e) => {
    e.preventDefault()
    await onCreate({ name, description })
    setName('')
    setDescription('')
  }

  return (
    <section className="panel">
      <h2>Playlist</h2>
      <form className="stack-form" onSubmit={submit}>
        <input value={name} onChange={(e) => setName(e.target.value)} placeholder="Nome playlist" required />
        <textarea value={description} onChange={(e) => setDescription(e.target.value)} placeholder="Descrizione" rows="3" />
        <button>Crea playlist</button>
      </form>

      {selectedTrack && <p className="helper-text">Brano selezionato: <strong>{selectedTrack.title}</strong></p>}

      <div className="playlist-list">
        {playlists.map((playlist) => (
          <div key={playlist.id} className="playlist-item">
            <div>
              <strong>{playlist.name}</strong>
              <span>{playlist.tracks.length} brani</span>
            </div>
            {selectedTrack && <button onClick={() => onAddTrack(playlist.id, selectedTrack)}>Aggiungi</button>}
          </div>
        ))}
      </div>
    </section>
  )
}
