import { createContext, useContext, useMemo, useState } from 'react'

const PlayerContext = createContext(null)

export function PlayerProvider({ children }) {
  const [currentTrack, setCurrentTrack] = useState(null)
  const [queue, setQueue] = useState([])

  const playTrack = (track, tracks = []) => {
    setCurrentTrack(track)
    setQueue(tracks)
  }

  const playNext = () => {
    if (!currentTrack || queue.length === 0) return
    const index = queue.findIndex((item) => item.id === currentTrack.id || item.externalTrackId === currentTrack.externalTrackId)
    if (index >= 0 && index < queue.length - 1) {
      setCurrentTrack(queue[index + 1])
    }
  }

  const value = useMemo(() => ({ currentTrack, queue, playTrack, playNext }), [currentTrack, queue])
  return <PlayerContext.Provider value={value}>{children}</PlayerContext.Provider>
}

export function usePlayer() {
  return useContext(PlayerContext)
}
