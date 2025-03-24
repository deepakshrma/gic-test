const apiEndpoint = "http://localhost:8080";

function parseTicket(data, isReserved) {
    setStatus(`
            ${isReserved ? 'You have reserved seats\n' : ''}
            Booking id: ${data.id}\n
            ${isReserved ? 'Please proceed to reserve the booking id.\n' : ''}
        `);
    document.querySelector("#booking_id").value = data.id;
    for (let seat of data.seats) {
        const s = document.querySelector(`.seat__${seat.row}__${seat.column}`);
        s.classList.remove("empty", "bookd");
        s.classList.add("resrv");
    }
}

function setStatus(s) {
    const status = document.querySelector(".status")
    status.textContent = s;
}

async function refresh() {
    fetch(`${apiEndpoint}/booking/seats`).then((r) => r.json()).then((data) => {
        const seats = document.getElementById("seats")
        seats.innerHTML = "<h1 class='center'>SCREEN</h1>"
        const [rows, cols] = [data.length, data[0].length];
        for (let r in data) {
            const dr = document.createElement("div");
            dr.setAttribute("class", "row");
            for (let c in data[r]) {
                const type = data[r][c];
                const dc = document.createElement("div");
                dc.classList.add("seat")
                dc.classList.add(`seat__${r}__${c}`)
                dc.classList.add(type.toLowerCase())
                dc.textContent = `[${String.fromCharCode('A'.charCodeAt(0) + rows - 1 - r)}${String(Number(c) + 1).padStart(2, "0")}]`;
                dr.appendChild(dc);
            }
            seats.appendChild(dr);
        }
    })
}

async function reserve() {
    const count = document.getElementById("ticket-count").value
    fetch(`${apiEndpoint}/booking/reserve`, {
        headers: {"Content-Type": "application/json"},
        method: "POST",
        body: JSON.stringify({count, start: ""})
    }).then((r) => r.json()).then((data) => parseTicket(data, true))
}

async function booknow() {
    const bookingId = document.querySelector("#booking_id").value
    if (!bookingId) return
    fetch(`${apiEndpoint}/booking/bookit/${bookingId}`, {
        headers: {"Content-Type": "application/json"},
        method: "PATCH"
    }).then((r) => r.json()).then((data) => {
        refresh()
    })
}

async function checknow() {
    const id = document.getElementById("ticketid").value
    try {
        const response = await fetch(`${apiEndpoint}/booking/seats/${id}`);
        if (!response.ok) {
            throw new Error(await response.text());
        }
        const data = await response.json();
        parseTicket(data);
    } catch (error) {
        setStatus(error+ ` with id: ${id}`);
    }
}

function css(c, type, ...elms) {
    elms.forEach(elm => {
        elm.classList[type]("hide")
    })
}

function init() {
    refresh()
    document.getElementById("menu-option").addEventListener("change", ({target: {value}}) => {
        const count = document.getElementById("ticket-count")
        const id = document.getElementById("ticketid")
        const resv = document.querySelector(".control .reserve")
        const bkit = document.querySelector(".control .booknow")
        const checknow = document.querySelector(".control .checknow")

        const status = document.querySelector(".status")
        status.textContent = ``
        if (value === '2') {
            css("hide", "add", count, resv, bkit);
            css("hide", "remove", id, checknow);
        } else {
            css("hide", "remove", count, resv, bkit);
            css("hide", "add", id, checknow);
        }
    })
    document.getElementById("ticket-count").addEventListener("keyup", ({target: {value}}) => {
        console.log(document.querySelector(".control .btn"))
        document.querySelector(".control .reserve").disabled = Number(value) <= 0;
        document.querySelector(".control .booknow").disabled = Number(value) <= 0;
    })
}

init()