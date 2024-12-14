const config = require("./utils/config")
const express = require("express")
require("express-async-errors")
const app = express()
const cors = require("cors")
const usersRouter = require("./controllers/user")
const eventsRouter = require("./controllers/event")
const loginRouter = require("./controllers/login")
const middleware = require("./utils/middleware")
const logger = require("./utils/logger")
const mongoose = require("mongoose")
const path = require("path")

mongoose.set("strictQuery", false)

logger.info("connecting to", config.MONGODB_URI)

mongoose
  .connect(config.MONGODB_URI)
  .then(() => {
    logger.info("connected to MongoDB")
  })
  .catch((error) => {
    logger.error("error connecting to MongoDB:", error.message)
  })

app.use(cors())
app.use(express.static("dist"))
app.use(express.json())
app.use(middleware.requestLogger)

app.use("/api/users", usersRouter)
app.use("/api/events", eventsRouter)
app.use("/api/login", loginRouter)

app.get("*", (req, res) => {
  res.sendFile(path.resolve(__dirname, "dist", "index.html"))
})

app.use(middleware.unknownEndpoint)
app.use(middleware.errorHandler)

module.exports = app
