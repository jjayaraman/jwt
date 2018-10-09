const express = require('express');
const router = express.Router();

const jwt = require('jsonwebtoken');

router.get('/', (req, res) => {
    res.send('API context working..');
})

router.post('/login', (req, res) => {
    const user = {
        'user': 'jjay',
        'email': 'jj@gmail.com'
    }

    jwt.sign(user, 'secret', { expiresIn: 600 }, (err, token) => {
        if (err) {
            console.log(err);
            res.sendStatus(403);
        } else {
            console.log('token :: ' + token);
            res.send(token);
        }

    })
})



router.get('/secured', validate, (req, res) => {
    //   console.log("xxx : " +err);
    res.send('secured api called succsfully.');
})

// JWT validate helper
function validate(req, res, next) {
    const bearer = req.headers['authorization'];
    if (typeof bearer !== 'undefined') {
        const token = bearer.split(' ')[1];
        jwt.verify(token, 'secret', (err, result) => {
            if (err) {
                console.log('error verify : ' + JSON.stringify(err));
                res.status(403).send(err);
            } else {
                console.log('Token verified succesfully..');
                next();
            }
        })
    } else {
        res.status(403).send('No authorization headers found.')
    }
}

module.exports = router;