import { usePlayer } from '../context/PlayerContext'

function formatTime(seconds = 0) {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${mins}:${String(secs).padStart(2, '0')}`
}

export default function PlayerBar() {
  const { currentTrack, playNext } = usePlayer()

  return (
    <div className="player-bar">
      {currentTrack ? (
        <>
          <div className="player-meta">
            <img src={currentTrack.coverUrl || 'https://placehold.co/80x80?text=♪'} alt={currentTrack.title} />
            <div>
              <strong>{currentTrack.title}</strong>
              <span>{currentTrack.artist}</span>
            </div>
          </div>
          <div className="player-controls">
            <audio key={currentTrack.audioUrl} controls autoPlay onEnded={playNext} src={currentTrack.audioUrl} />
          </div>
          <div className="player-extra">
            <span>{formatTime(currentTrack.durationSeconds || 0)}</span>
            <span>{currentTrack.source || 'LIBRARY'}</span>
          </div>
        </>
      ) : (
        <div className="player-empty">Seleziona un brano per iniziare la riproduzione.</div>
      )}
    </div>
  )
}
