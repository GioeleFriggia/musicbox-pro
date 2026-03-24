import { useEffect, useState } from 'react'
import client from '../api/client'
import PlaylistPanel from '../components/PlaylistPanel'
import SearchBar from '../components/SearchBar'
import TrackCard from '../components/TrackCard'

function toTrackPayload(track) {
  return {
    externalTrackId: String(track.id || track.externalTrackId),
    title: track.title,
    artist: track.artist,
    album: track.album,
    audioUrl: track.audioUrl,
    coverUrl: track.coverUrl,
    durationSeconds: track.durationSeconds || 0,
  }
}

export default function SearchPage() {
  const [tracks, setTracks] = useState([])
  const [playlists, setPlaylists] = useState([])
  const [loading, setLoading] = useState(false)
  const [selectedTrack, setSelectedTrack] = useState(null)
  const [message, setMessage] = useState('')

  const loadPlaylists = async () => {
    const { data } = await client.get('/playlists')
    setPlaylists(data)
  }

  useEffect(() => {
    loadPlaylists()
  }, [])

  const searchTracks = async (query) => {
    setLoading(true)
    setMessage('')
    try {
      const { data } = await client.get('/music/search', { params: { query } })
      setTracks(data)
    } catch (err) {
      setMessage(err.response?.data?.message || 'Errore durante la ricerca')
    } finally {
      setLoading(false)
    }
  }

  const createPlaylist = async (payload) => {
    await client.post('/playlists', payload)
    await loadPlaylists()
  }

  const addFavorite = async (track) => {
    await client.post('/favorites', toTrackPayload(track))
    setMessage(`Aggiunto ai preferiti: ${track.title}`)
  }

  const addToPlaylist = async (playlistId, track) => {
    await client.post(`/playlists/${playlistId}/tracks`, toTrackPayload(track))
    setMessage(`Aggiunto a playlist: ${track.title}`)
    await loadPlaylists()
  }

  return (
    <div className="page-grid">
      <section>
        <div className="hero-card">
          <h1>Scopri musica indipendente con audio migliore di YouTube</h1>
          <p>Cerca su Jamendo, arricchisci con Discogs, salva in playlist e preferiti.</p>
          <SearchBar onSearch={searchTracks} loading={loading} />
          {message && <p className="helper-text">{message}</p>}
        </div>

        <div className="track-grid">
          {tracks.map((track) => (
            <TrackCard
              key={track.id}
              track={track}
              onFavorite={addFavorite}
              onAddToPlaylist={(item) => setSelectedTrack(item)}
            />
          ))}
        </div>
      </section>

      <PlaylistPanel
        playlists={playlists}
        selectedTrack={selectedTrack}
        onCreate={createPlaylist}
        onAddTrack={addToPlaylist}
      />
    </div>
  )
}
