import React, { useEffect, useState } from 'react'
import SubscribeModal from './SubscribeModal'
import './css/Subscription.css'
import {Oval} from "react-loader-spinner"

const Subscription = () => {
    const [query, setQuery] = useState("")
    const [foundGames, setFoundGames] = useState([])
    const [selectedGame, setSelectedGame] = useState(null)
    const [errorMsg, setErrorMsg] = useState("")
    const [loading, setLoading] = useState(false)
    console.log(foundGames)

    
    function handleSearchResults() {
        if (query.length >= 3) {
            searchGame(query)
        } else if (query.length === 0) {
            setFoundGames([])
        }
    }


    useEffect(() => {
        setLoading(true)
        const timeoutId = setTimeout(() => {
            handleSearchResults()
            setLoading(false)
        }, 1500)

        return () => clearTimeout(timeoutId)
    }, [query])


    async function searchGame(input) {
        if (errorMsg) {
            setErrorMsg("")
        }

        query.trim()
        try {
            const response = await fetch(`http://localhost:8080/api/games?search=${input}`)

            if(response.ok) {
                const games = await response.json()
                setFoundGames(games)
            } else {
                console.log("else")
                const errorMessage = await response.text()
                console.log(errorMessage)
            }
        } catch (error) {
            console.log(error)
            setErrorMsg("An error occurred. Please try again later")
        }
        
    }

    return (
        <div className='search-container'>
            <input
                className='search-input'
                type="text" 
                placeholder='Enter the title of the game'
                value={query}
                onChange={(event) => {
                    setQuery(event.target.value)
                }}
            />
            {foundGames.length !== 0 &&
                <div className='found-games-container'>
                    {foundGames.map((game, index) => (
                        <div className='found-game-container' key={index}>
                            <div className='found-game-title'>{game.name}</div>
                            <button 
                                onClick={() => {
                                    setSelectedGame(game.name)

                                }}
                            >
                                Subscribe
                            </button>
                        </div>
                    ))}
                </div>
            }

            
            {loading && foundGames.length === 0 &&
                <div className='loading-container'>
                    <Oval
                        visible={true}
                        height="50"
                        width="50"
                        color="#4fa94d"
                        ariaLabel="oval-loading"
                        wrapperStyle={{}}
                        wrapperClass=""
                    />
                </div>
            }
            
            {errorMsg && 
                <div className='error-msg'>
                    {errorMsg}
                </div>
            }

            {selectedGame !== null && 
                <SubscribeModal 
                    gameTitle={selectedGame}
                    onClose={() => setSelectedGame(null)}
                />
            }
        </div>
        
    )
}

export default Subscription
