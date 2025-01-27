import React, { useState } from 'react'

const SubscribeModal = ({gameTitle, onClose}) => {
    const [email, setEmail] = useState("")
    const [price, setPrice] = useState("")
    const [errorMsg, setErrorMsg] = useState("")
    const [successMsg, setSuccessMsg] = useState("")

    async function subscribeToGame() {

        if (errorMsg) {
            setErrorMsg("")
        }
        if (successMsg) {
            setSuccessMsg("")
        }
        
        if (!email || !price) {
            setErrorMsg("Please fill out all the fields")
            console.log("please fill out all the fields")
            return
        }

        

        try {
            const response = await fetch("http://localhost:8080/api/subscription", 
                {
                    method: "POST",
                    // headers: {"Content-Type": "application/json"},
                    body: JSON.stringify(
                        {
                            email: email,
                            priceThreshold: price,
                            gameTitle: gameTitle
                        }
                    )
                        
                })
            
            if (response.ok) {
                setSuccessMsg("Subscription successful")
                setEmail("")
                setPrice("")
            } else {
                const message = await response.text()
                console.log(message)
                setErrorMsg(message)
            }
        } catch (error) {
            console.log(error)
            setErrorMsg("Something went wrong. Please try again.")
        }

    }

    return (
        <div className='subscribe-modal-overlay'>
            <div className='subscribe-modal-content'>
                <h2>{gameTitle}</h2>
                <p>After entering the below information, you will receive an email notification when the price of the videogame drops below the specified price</p>
                <div className='subscribe-modal-group'>
                    <input 
                        type="text" 
                        placeholder='Enter your e-mail address'
                        value={email}
                        onChange={(event) => setEmail(event.target.value)}
                    />
                    <input 
                        type="number" 
                        placeholder='Enter the price'
                        value={price}
                        onChange={(event) => setPrice(event.target.value)}
                    />
                    <button 
                        className='submit-sub-btn'
                        onClick={() => subscribeToGame()}
                        >
                        Subscribe
                    </button>
                    {successMsg &&
                        <div className='success-msg'>
                            {successMsg}
                        </div>
                    }
                    {errorMsg &&
                        <div className='error-msg'>
                            {errorMsg}
                        </div>
                    }
                </div>

                <button className='close-modal-btn' onClick={onClose}>
                    X
                </button>
            </div>       
        </div>
    )
}

export default SubscribeModal
