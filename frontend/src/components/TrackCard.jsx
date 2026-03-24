import { usePlayer } from '../context/PlayerContext'

function formatTime(seconds = 0) {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${mins}:${String(secs).padStart(2, '0')}`
}

export default function TrackCard({ track, onFavorite, onAddToPlaylist, compact = false }) {
  const { playTrack } = usePlayer()

  return (
    <article className={`track-card ${compact ? 'compact' : ''}`}>
      <img src={track.coverUrl || 'https://placehold.co/300x300?text=Music'} alt={track.title} />
      <div className="track-info">
        <h3>{track.title}</h3>
        <p>{track.artist}</p>
        {track.album && <span>{track.album}</span>}
        <small>
          {track.genre || 'Indie / CC'} • {formatTime(track.durationSeconds || 0)}
        </small>
      </div>
      <div className="track-actions">
        <button onClick={() => playTrack(track)}>Play</button>
        {onFavorite && <button className="secondary" onClick={() => onFavorite(track)}>❤</button>}
        {onAddToPlaylist && <button className="secondary" onClick={() => onAddToPlaylist(track)}>+ Playlist</button>}
      </div>
    </article>
  )
}
