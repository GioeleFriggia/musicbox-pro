import { useEffect, useState } from 'react'
import client from '../api/client'
import TrackCard from '../components/TrackCard'

export default function LibraryPage() {
  const [favorites, setFavorites] = useState([])
  const [playlists, setPlaylists] = useState([])

  const loadAll = async () => {
    const [favoritesRes, playlistsRes] = await Promise.all([
      client.get('/favorites'),
      client.get('/playlists'),
    ])
    setFavorites(favoritesRes.data)
    setPlaylists(playlistsRes.data)
  }

  useEffect(() => {
    loadAll()
  }, [])

  const removeFavorite = async (track) => {
    await client.delete(`/favorites/${track.externalTrackId}`)
    await loadAll()
  }

  const removeFromPlaylist = async (playlistId, trackId) => {
    await client.delete(`/playlists/${playlistId}/tracks/${trackId}`)
    await loadAll()
  }

  const deletePlaylist = async (playlistId) => {
    await client.delete(`/playlists/${playlistId}`)
    await loadAll()
  }

  return (
    <div className="library-page">
      <section className="panel">
        <h1>I tuoi preferiti</h1>
        <div className="track-grid compact-grid">
          {favorites.map((track) => (
            <TrackCard key={track.id} track={track} compact onFavorite={removeFavorite} />
          ))}
        </div>
      </section>

      <section className="panel">
        <h1>Le tue playlist</h1>
        <div className="playlist-columns">
          {playlists.map((playlist) => (
            <div key={playlist.id} className="playlist-column">
              <div className="playlist-header">
                <div>
                  <h2>{playlist.name}</h2>
                  <p>{playlist.description || 'Nessuna descrizione'}</p>
                </div>
                <button className="secondary" onClick={() => deletePlaylist(playlist.id)}>Elimina</button>
              </div>
              <div className="playlist-track-list">
                {playlist.tracks.map((track) => (
                  <div key={track.id} className="playlist-track-row">
                    <div>
                      <strong>{track.title}</strong>
                      <span>{track.artist}</span>
                    </div>
                    <button onClick={() => removeFromPlaylist(playlist.id, track.id)}>Rimuovi</button>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      </section>
    </div>
  )
}
