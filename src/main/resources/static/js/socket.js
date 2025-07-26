//const sock = new SockJS('/chess-websocket');
//let client = null;
//client = Stomp.over(sock);
//client.debug = null;
//
//const chess = new Chess();
//const boardElement = document.querySelector(".chessboard");
//const playerPlaying = document.querySelector(".currentPlayer");
//playerPlaying.innerText = "White turn";
//let draggedPiece = null;
//let sourceSquare = null;
//let targetSource = null;
// // Unique room ID for each game session
// let clientInstance = {
//     roomId: null,
//     gameSubscription: null,
//     playerRole: null,
// };
//
//const renderBoard = () => {
//    const board = chess.board();
//    boardElement.innerHTML = "";
//    board.forEach((row, rowindex) => {
//        row.forEach((square, squareindex) => {
//            const squareElement = document.createElement("div");
//            squareElement.classList.add("square", (rowindex + squareindex) % 2 === 0 ? "light" : "dark");
//            squareElement.dataset.row = rowindex;
//            squareElement.dataset.col = squareindex;
//
//            if (square) {
//                const pieceElement = document.createElement("div");
//                pieceElement.classList.add("piece", square.color === "w" ? "white" : "black");
//                pieceElement.innerText = getPieceUnicode(square);
//                pieceElement.draggable = clientInstance.playerRole === square.color;
//
//                pieceElement.addEventListener("dragstart", (e) => {
//                    if (pieceElement.draggable) {
//                        draggedPiece = pieceElement;
//                        sourceSquare = { row: rowindex, col: squareindex };
//                        e.dataTransfer.setData("text/plain", "");
//                    }
//                });
//
//                pieceElement.addEventListener("dragend", (e) => {
//                    draggedPiece = null;
//                    sourceSquare = null;
//                });
//
//                squareElement.appendChild(pieceElement);
//            }
//
//            squareElement.addEventListener("dragover", (e) => e.preventDefault());
//
//            squareElement.addEventListener("drop", (e) => {
//                e.preventDefault();
//                if (draggedPiece) {
//                    targetSource = {
//                        row: parseInt(squareElement.dataset.row),
//                        col: parseInt(squareElement.dataset.col),
//                    };
//                    handleMove(sourceSquare, targetSource);
//                }
//            });
//
//            boardElement.appendChild(squareElement);
//        });
//    });
//};
//
//const handleMove = (source, target) => {
//    const move = {
//        from: `${String.fromCharCode(97 + source.col)}${8 - source.row}`,
//        to: `${String.fromCharCode(97 + target.col)}${8 - target.row}`,
//        fen: chess.fen(), // Get the current board state in FEN format
//    };
//
//    const validMove = chess.move(move); // Validate the move locally
//    if (validMove && clientInstance.roomId) {
//        chess.undo(); // Undo the move locally (server will handle the actual move)
//        const payload = {
//            ...move,
//            roomId: clientInstance.roomId, // Attach the current room ID
//        };
//        client.send("/app/move", {}, JSON.stringify(payload)); // Send the move to the server
//    } else {
//        renderBoard(); // Re-render the board if the move is invalid
//        console.error("Invalid move");
//    }
//};
//
//const getPieceUnicode = (piece) => {
//    const unicodePieces = {
//        p: '\u265F', // Black pawn ♟
//        r: '\u265C', // Black rook ♜
//        n: '\u265E', // Black knight ♞
//        b: '\u265D', // Black bishop ♝
//        q: '\u265B', // Black queen ♛
//        k: '\u265A', // Black king ♚
//        P: '\u2659', // White pawn ♙
//        R: '\u2656', // White rook ♖
//        N: '\u2658', // White knight ♘
//        B: '\u2657', // White bishop ♗
//        Q: '\u2655', // White queen ♕
//        K: '\u2654', // White king ♔
//    };
//    return unicodePieces[piece.type] || "";
//};
//
//renderBoard();
//
//// Each client instance will manage its own roomId and gameSubscription
//
//
//client.connect({}, (frame) => {
//    console.log("Connected:", frame);
//
//    // Send a join request to the server
//    client.send("/app/join", {}, JSON.stringify({}));
//
//    // Subscribe to the playerRole updates
//    client.subscribe("/topic/playerRole", (message) => {
//        try {
//            const response = JSON.parse(message.body);
//
//            if (response.roomId) {
//                // Assign roomId to this client's instance
//                clientInstance.roomId = response.roomId;
//                console.log("Room ID set to:", clientInstance.roomId);
//
//                // Unsubscribe from the previous game topic if already subscribed
////                if (clientInstance.gameSubscription) {
////                    clientInstance.gameSubscription.unsubscribe();
////                }
//
//                // Subscribe to the updated game topic for this specific room
//                clientInstance.gameSubscription = client.subscribe(
//                    `/topic/game/${clientInstance.roomId}`,
//                    (gameMessage) => {
//                        const gameResponse = JSON.parse(gameMessage.body);
//                        console.log("Game update:", gameResponse);
//
//                        if (gameResponse.valid) {
//                            const move = {
//                                from: gameResponse.from,
//                                to: gameResponse.to,
//                            };
//
//                            // Apply the move locally and update the board
//                            chess.move(move);
//                            playerPlaying.innerText =
//                                gameResponse.turn === "b" ? "Black turn" : "White turn";
//                            renderBoard();
//                        } else {
//                            console.error("Invalid move received from server");
//                        }
//                    }
//                );
//            }
//
//            if (!clientInstance.playerRole && response.role) {
//                // Assign player role to this client instance
//                clientInstance.playerRole = response.role;
//
//                // Flip the board if the role is "black"
//                if (clientInstance.playerRole === "b") {
//                    boardElement.classList.add("flipped");
//                } else {
//                    boardElement.classList.remove("flipped");
//                }
//
//                // Handle spectators separately
//                if (clientInstance.playerRole === "spectator") {
//                    clientInstance.playerRole = null;
//                }
//
//                renderBoard();
//            }
//        } catch (error) {
//            console.error("Error processing player role:", error);
//        }
//    });
//}, (error) => {
//    console.error("Connection error:", error);
//});
//
//// Handle WebSocket errors
//client.onerror = (error) => {
//    console.error("WebSocket error:", error);
//};
//
//// Handle WebSocket disconnection
//const handleLeave = () => {
//    if (client && client.connected) {
//        // Send a leave message to the server
//        client.send("/app/leave", {}, JSON.stringify({}));
//        client.disconnect(() => {
//            console.log("Disconnected from WebSocket");
//        });
//    }
//};
//
//// Attach event listeners to handle user leaving the page or closing the tab
//window.addEventListener("beforeunload", handleLeave);
//window.addEventListener("unload", handleLeave);











class ChessClient {
    constructor(client, chess, boardElement, playerPlaying) {
        this.client = client; // WebSocket STOMP client
        this.pieceDragable=null;
        this.chess = chess; // Chess.js instance
        this.boardElement = boardElement; // DOM element for the chessboard
        this.playerPlaying = playerPlaying; // DOM element for displaying the current player

        this.roomId = null; // Unique room ID for this game session
        this.gameSubscription = null; // STOMP subscription for game updates
        this.playerRole = null; // Role of the player (white, black, spectator)
        this.isInitialized = false; // Tracks if the roomId has been set for this instance
        this.time = null; // Total time for the game
        this.isTimeInitialized = false; // Tracks if the timer has been initialized
        this.isTimerStarted = false; // Tracks if the timer has started
        this.minutes = null; // Remaining minutes
        this.seconds = 0; // Remaining seconds
        this.intervalId = null; // Interval ID for the timer
        this.map=new Map();
        this.currentTurn="w";
        this.gameOver=null;
//        const nodeList = document.querySelectorAll(".example");
        this.timeElement = document.querySelectorAll(".time"); // DOM element for the timer display
        this.playerRemoved=false;
    }

    connect() {
        this.client.connect({}, (frame) => {
            console.log("Connected:", frame);


            // Subscribe to the playerRole updates
            this.client.subscribe("/topic/playerRole", (message) => {
                            try {
                                const response = JSON.parse(message.body);
                                       console.log(response.role);
                                if (response.time && !this.isTimeInitialized) {
                                    this.time = response.time;
                                    this.minutes = this.time;
                                    this.isTimeInitialized = true;
                                }

                                  if (!this.playerRole && response.role) {
                                                        this.playerRole = response.role;

                                                        // Flip the board if the role is "black"
                                                        if (this.playerRole === "b") {
                                                        const chessPlayers=document.getElementsByClassName("time");
                                                      for(let i=0;i<chessPlayers.length;i++){
                                                      if(i%2==0)
                                                        chessPlayers[i].id="w";
                                                        else
                                                        chessPlayers[i].id="b";

                                                         }
                                                         const playerTime=document.getElementById("b");
                                                          playerTime.innerText=this.time+" :"+" 00";
                                                            this.boardElement.classList.add("flipped");
                                                        } else {


                                                        const chessPlayers=document.getElementsByClassName("time");


                                                        for(let i=0;i<chessPlayers.length;i++){
                                                            if(i%2==0)
                                                             chessPlayers[i].id="b";
                                                             else
                                                                chessPlayers[i].id="w";

                                                                  }

                                                                  const playerTime=document.getElementById("b");
                                                                  playerTime.innerText=this.time+" :"+" 00";

                                                            this.boardElement.classList.remove("flipped");
                                                        }

                                                        // Handle spectators separately
                                                        if (this.playerRole === "spectator") {
                                                            this.playerRole = null;
                                                        }
                                                  console.log("Reached here...")
                                                        this.renderBoard();
                                                    }

                                // Process the response if roomId is provided and not yet initialized
                                if (response.roomId && !this.isInitialized && this.time === response.time) {
                                    this.roomId = response.roomId;
                                    this.isInitialized = true; // Mark this instance as initialized


                                   let Timeobj={
                                     seconds:0,
                                     minutes:this.time,
                                             }


                                   let Timeobj1={
                                     seconds:0,
                                     minutes:this.time,
                                             }
                                 this.map.set("b",Timeobj);
                                 this.map.set("w",Timeobj1);



            //                       if(player.role=="b"){
            //                                         let blackTimeobj={
            //                                           seconds:0,
            //                                             minutes:response.time,
            //                                                      }
            //                                            this.map.set("b",blackTimeobj);
            //                                         }
            //                                         if(response.role=="w" ){
            //                                           let whiteTimeobj={
            //                                                               seconds:0,
            //                                                                 minutes:response.time,
            //                                                       }
            //                                                                this.map.set("w",whiteTimeobj);
            //                                         }
                                    // Unsubscribe from the previous game topic if already subscribed
                                    if (this.gameSubscription) {
                                        this.gameSubscription.unsubscribe();
                                    }

                                    // Subscribe to the updated game topic for this specific room
                                    this.gameSubscription = this.client.subscribe(
                                        `/topic/game/${this.time}/${this.roomId}`,
                                        (gameMessage) => {

                                            const gameResponse = JSON.parse(gameMessage.body);
                                            console.log("Game update:", gameResponse);

                                            if (gameResponse.valid) {
                                                const move = {
                                                    from: gameResponse.from,
                                                    to: gameResponse.to,
                                                };

                                                this.currentTurn=gameResponse.turn;
                                                 clearInterval(this.intervalId);
                                                 this.startTimer();
                                                  // Apply the move locally and update the board
                                                this.chess.move(move);
                                                this.playerPlaying.innerText =
                                                    gameResponse.turn === "b" ? "Black turn" : "White turn";
                                                this.renderBoard();
                                            } else {
                                                console.error("Invalid move received from server");
                                            }
                                        }
                                    );

                                    // Start the timer if it hasn't started already
                                    if (!this.isTimerStarted) {
                                        this.isTimerStarted = true;
                                        this.startTimer();
                                    }
                                }

                                // Assign player role if not already set

                            } catch (error) {
                                console.error("Error processing player role:", error);
                            }
                        });

            // Send a join request to the server
            this.client.send("/app/join", {}, JSON.stringify({}));


        }, (error) => {
            console.error("Connection error:", error);
        });
    }
findTime() {
    if ((this.minutes === 0 && this.seconds === 0) || this.playerRemoved)
         {
            // Time is up
            let container = document.querySelector("body");
            let gameOverElement = document.createElement("div");
            let spanElement = document.createElement("span");
            let buttonElement = document.createElement("button");

            // Styling and message setup
            gameOverElement.classList.add("gameOver");
            spanElement.innerText = "Game Over";
            buttonElement.innerText = `Start new ${this.time} min`;

            // Append elements
            gameOverElement.append(spanElement, buttonElement);
            container.appendChild(gameOverElement);

            // Disable dragging pieces
            const pieces = document.querySelectorAll(".chess-piece");
            pieces.forEach(piece => {
                piece.draggable = false;
            });

            // Add event listener with bound context
            buttonElement.addEventListener("click", this.reconnect.bind(this));

            // Stop the timer
            clearInterval(this.intervalId);
            return;
        }


        if (this.seconds === 0) {
             if (this.minutes > 0) {
                 this.minutes -= 1;
                 this.seconds = 59;
             }
             }
     else {
        this.seconds -= 1;
    }

    // Update the timer display
    const formattedMinutes = this.minutes.toString().padStart(2, "0");
    const formattedSeconds = this.seconds.toString().padStart(2, "0");
    const newTime = `${formattedMinutes} : ${formattedSeconds}`;
//    b ,w
     const playerTime=document.getElementById(this.currentTurn);
     playerTime.innerText=newTime;

      // Update the Map with the current player's time
         const timerObject = this.map.get(this.currentTurn);
         timerObject.minutes = this.minutes;
         timerObject.seconds = this.seconds;
         this.map.set(this.currentTurn, timerObject);

}

reconnect() {
    console.log("Reached here");

    let times = this.time; // Ensure 'this.time' is accessible
    const timerData = { time: times };

    fetch('/newGame', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json', // Indicate JSON payload
        },
        body: JSON.stringify(timerData), // Convert object to JSON
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to set timer');
            }
            return response.text(); // Handle plain text response (e.g., redirect URL)
        })
        .then(data => {
            console.log('Timer set successfully. Redirecting to:', data);
            window.location.href = "/game"; // Manually redirect to /game
        })
        .catch(error => {
            console.error('Error setting timer:', error);
            alert('Failed to start the game. Please try again.');
        });
}


    startTimer() {


            // Set initial values for minutes and seconds
           const timerObject=this.map.get(this.currentTurn);
           this.minutes=timerObject.minutes;
           this.seconds=timerObject.seconds;
            // Save the interval ID for clearing later
            this.intervalId = setInterval(() => this.findTime(), 1000); // Ensure `this` context is preserved
            timerObject.minutes=this.minutes;
            timerObject.seconds=this.seconds;
            console.log(timerObject.minutes);
            console.log(timerObject.minutes);

    }

    handleMove(source, target) {
        const move = {
            from: `${String.fromCharCode(97 + source.col)}${8 - source.row}`,
            to: `${String.fromCharCode(97 + target.col)}${8 - target.row}`,
            fen: this.chess.fen(), // Get the current board state in FEN format
        };

        const validMove = this.chess.move(move); // Validate the move locally
        if (validMove && this.roomId) {

            this.chess.undo(); // Undo the move locally (server will handle the actual move)

            const payload = {
                ...move,
                roomId: this.roomId, // Attach the current room ID
                time: this.time,
            };
            this.client.send("/app/move", {}, JSON.stringify(payload)); // Send the move to the server
        } else {
//            this.renderBoard(); // Re-render the board if the move is invalid
            console.error("Invalid move");
        }
    }

    renderBoard() {
        const board = this.chess.board();
        this.boardElement.innerHTML = "";

        this.draggedPiece = null;
        this.sourceSquare = null;
        board.forEach((row, rowindex) => {
            row.forEach((square, squareindex) => {
                const squareElement = document.createElement("div");
                squareElement.classList.add(
                    "square",
                    (rowindex + squareindex) % 2 === 0 ? "light" : "dark"
                );
                squareElement.dataset.row = rowindex;
                squareElement.dataset.col = squareindex;

                if (square) {
                    const pieceElement = document.createElement("div");
                    this.pieceDragable=pieceElement;
                    pieceElement.classList.add(
                        "piece",
                        "chess-piece",
                        square.color === "w" ? "white" : "black",
                        square.type === 'p' ? "pawn":"notpawn" // No trailing comma here
                    );
                    pieceElement.innerText = this.getPieceUnicode(square);
                    pieceElement.draggable = this.playerRole === square.color;

                    pieceElement.addEventListener("dragstart", (e) => {
                        if (pieceElement.draggable) {
                            this.draggedPiece = pieceElement;
                            this.sourceSquare = { row: rowindex, col: squareindex };
                            e.dataTransfer.setData("text/plain", "");
                        }
                    });

                    pieceElement.addEventListener("dragend", () => {
                        this.draggedPiece = null;
                        this.sourceSquare = null;
                    });

                    squareElement.appendChild(pieceElement);
                }

                squareElement.addEventListener("dragover", (e) => e.preventDefault());

                squareElement.addEventListener("drop", (e) => {
                    e.preventDefault();
                    if (this.draggedPiece) {
                        const targetSource = {
                            row: parseInt(squareElement.dataset.row),
                            col: parseInt(squareElement.dataset.col),
                        };
                        this.handleMove(this.sourceSquare, targetSource);
                    }
                });

                this.boardElement.appendChild(squareElement);
            });
        });
    }

    getPieceUnicode(piece) {
        const unicodePieces = {
            p: "\u265F", // Black pawn ♟
            r: "\u265C", // Black rook ♜
            n: "\u265E", // Black knight ♞
            b: "\u265D", // Black bishop ♝
            q: "\u265B", // Black queen ♛
            k: "\u265A", // Black king ♚
            P: "\u2659", // White pawn ♙
            R: "\u2656", // White rook ♖
            N: "\u2658", // White knight ♘
            B: "\u2657", // White bishop ♗
            Q: "\u2655", // White queen ♕
            K: "\u2654", // White king ♔
        };
        return unicodePieces[piece.type] || "";
    }

    disconnect() {
    console.log(`/stopGame/${this.roomId}`);
        if (this.client && this.client.connected) {
            // Send a leave message to the server
            const send = {
                time: this.time,
                roomId:this.roomId,
                playerRole:this.playerRole
            };

 this.client.subscribe(`/stopGame/${this.roomId}`,(response=>{
 " sucess && removed player "
 if(response.sucess){
const time =document.getElementById(response.removedPlayer);
this.playerRemoved=true;
this.findTime(playerRemoved);
 }
 }))
            this.client.send("/app/leave", {}, JSON.stringify(send));
            this.client.disconnect(() => {
                console.log("Disconnected from WebSocket");
            });
        }
    }
}

// Initializing the globals
const boardElement = document.querySelector(".chessboard");
const playerPlaying = document.querySelector(".currentPlayer");
const chess = new Chess();
playerPlaying.innerText = "White turn";

// Initialize the ChessClient
const sock = new SockJS("/chess-websocket");
const client = Stomp.over(sock);
client.debug = null;

// Connecting the client
const chessClient = new ChessClient(client, chess, boardElement, playerPlaying);
chessClient.connect();

// Handle page unload
window.addEventListener("beforeunload", () => chessClient.disconnect());
window.addEventListener("unload", () => chessClient.disconnect());










//
//
//
//
//
//class ChessClient {
//    constructor(sockJsUrl) {
//        this.sock = new SockJS(sockJsUrl);
//        this.client = Stomp.over(this.sock);
//        this.client.debug = null; // Disable debug logging
//        this.chess = new Chess(); // Local chess instance
//        this.boardElement = document.querySelector(".chessboard");
//        this.playerPlaying = document.querySelector(".currentPlayer");
//        this.playerPlaying.innerText = "White turn";
//        this.roomId = null; // Unique room ID for this client
//        this.playerRole = null; // Role of the player (white, black, spectator)
//        this.gameSubscription = null; // Subscription to the game topic
//        this.draggedPiece = null;
//        this.sourceSquare = null;
//        this.targetSource = null;
//
//        // Initialize the chessboard
//        this.renderBoard();
//
//        // Connect to the WebSocket server
//        this.connectToServer();
//
//        // Handle page unload events
//        window.addEventListener("beforeunload", () => this.handleLeave());
//        window.addEventListener("unload", () => this.handleLeave());
//    }
//
//    renderBoard() {
//        const board = this.chess.board();
//        this.boardElement.innerHTML = "";
//        board.forEach((row, rowIndex) => {
//            row.forEach((square, squareIndex) => {
//                const squareElement = document.createElement("div");
//                squareElement.classList.add(
//                    "square",
//                    (rowIndex + squareIndex) % 2 === 0 ? "light" : "dark"
//                );
//                squareElement.dataset.row = rowIndex;
//                squareElement.dataset.col = squareIndex;
//
//                if (square) {
//                    const pieceElement = document.createElement("div");
//                    pieceElement.classList.add("piece", square.color === "w" ? "white" : "black");
//                    pieceElement.innerText = this.getPieceUnicode(square);
//                    pieceElement.draggable = this.playerRole === square.color;
//
//                    pieceElement.addEventListener("dragstart", (e) => {
//                        if (pieceElement.draggable) {
//                            this.draggedPiece = pieceElement;
//                            this.sourceSquare = { row: rowIndex, col: squareIndex };
//                            e.dataTransfer.setData("text/plain", "");
//                        }
//                    });
//
//                    pieceElement.addEventListener("dragend", () => {
//                        this.draggedPiece = null;
//                        this.sourceSquare = null;
//                    });
//
//                    squareElement.appendChild(pieceElement);
//                }
//
//                squareElement.addEventListener("dragover", (e) => e.preventDefault());
//
//                squareElement.addEventListener("drop", (e) => {
//                    e.preventDefault();
//                    if (this.draggedPiece) {
//                        this.targetSource = {
//                            row: parseInt(squareElement.dataset.row),
//                            col: parseInt(squareElement.dataset.col),
//                        };
//                        this.handleMove(this.sourceSquare, this.targetSource);
//                    }
//                });
//
//                this.boardElement.appendChild(squareElement);
//            });
//        });
//    }
//
//    handleMove(source, target) {
//        const move = {
//            from: `${String.fromCharCode(97 + source.col)}${8 - source.row}`,
//            to: `${String.fromCharCode(97 + target.col)}${8 - target.row}`,
//            fen: this.chess.fen(), // Get the current board state in FEN format
//        };
//
//        const validMove = this.chess.move(move); // Validate the move locally
//        if (validMove && this.roomId) {
//            this.chess.undo(); // Undo the move locally (server will handle the actual move)
//            const payload = {
//                ...move,
//                roomId: this.roomId, // Attach the current room ID
//            };
//            this.client.send("/app/move", {}, JSON.stringify(payload)); // Send the move to the server
//        } else {
//            this.renderBoard(); // Re-render the board if the move is invalid
//            console.error("Invalid move");
//        }
//    }
//
//    getPieceUnicode(piece) {
//        const unicodePieces = {
//            p: '\u265F', // Black pawn ♟
//            r: '\u265C', // Black rook ♜
//            n: '\u265E', // Black knight ♞
//            b: '\u265D', // Black bishop ♝
//            q: '\u265B', // Black queen ♛
//            k: '\u265A', // Black king ♚
//            P: '\u2659', // White pawn ♙
//            R: '\u2656', // White rook ♖
//            N: '\u2658', // White knight ♘
//            B: '\u2657', // White bishop ♗
//            Q: '\u2655', // White queen ♕
//            K: '\u2654', // White king ♔
//        };
//        return unicodePieces[piece.type] || "";
//    }
//
//    connectToServer() {
//        this.client.connect({}, (frame) => {
//            console.log("Connected:", frame);
//
//            // Send a join request to the server
//            this.client.send("/app/join", {}, JSON.stringify({}));
//
//            // Subscribe to the playerRole updates
//            this.client.subscribe("/topic/playerRole", (message) => {
//                try {
//                    const response = JSON.parse(message.body);
//
//                    if (response.roomId) {
//                        this.roomId = response.roomId;
//                        console.log("Room ID set to:", this.roomId);
//
//                        // Unsubscribe from the previous game topic if already subscribed
//                        if (this.gameSubscription) {
//                            this.gameSubscription.unsubscribe();
//                        }
//
//                        // Subscribe to the updated game topic for this specific room
//                        this.gameSubscription = this.client.subscribe(
//                            `/topic/game/${this.roomId}`,
//                            (gameMessage) => {
//                                const gameResponse = JSON.parse(gameMessage.body);
//                                console.log("Game update:", gameResponse);
//
//                                if (gameResponse.valid) {
//                                    const move = {
//                                        from: gameResponse.from,
//                                        to: gameResponse.to,
//                                    };
//
//                                    // Apply the move locally and update the board
//                                    this.chess.move(move);
//                                    this.playerPlaying.innerText =
//                                        gameResponse.turn === "b" ? "Black turn" : "White turn";
//                                    this.renderBoard();
//                                } else {
//                                    console.error("Invalid move received from server");
//                                }
//                            }
//                        );
//                    }
//
//                    if (!this.playerRole && response.role) {
//                        this.playerRole = response.role;
//
//                        // Flip the board if the role is "black"
//                        if (this.playerRole === "b") {
//                            this.boardElement.classList.add("flipped");
//                        } else {
//                            this.boardElement.classList.remove("flipped");
//                        }
//
//                        // Handle spectators separately
//                        if (this.playerRole === "spectator") {
//                            this.playerRole = null;
//                        }
//
//                        this.renderBoard();
//                    }
//                } catch (error) {
//                    console.error("Error processing player role:", error);
//                }
//            });
//        }, (error) => {
//            console.error("Connection error:", error);
//        });
//    }
//
//    handleLeave() {
//        if (this.client && this.client.connected) {
//            // Send a leave message to the server
//            this.client.send("/app/leave", {}, JSON.stringify({}));
//            this.client.disconnect(() => {
//                console.log("Disconnected from WebSocket");
//            });
//        }
//    }
//}
//
//// Create a new instance of ChessClient
//const chessClient = new ChessClient("/chess-websocket");

