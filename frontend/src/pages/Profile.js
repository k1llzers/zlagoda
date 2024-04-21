import React, {useEffect, useState} from 'react';
import {
    MDBCol,
    MDBContainer,
    MDBRow,
    MDBCard,
    MDBCardText,
    MDBCardBody,
    MDBCardImage,
} from 'mdb-react-ui-kit';
import axios from "axios";
import profile1 from "../images/profile1.jpg"
import profile2 from "../images/profile2.jpg"
import profile3 from "../images/profile3.jpg"
import StyledButton from "../styledComponent/styldButton";
import {Dialog, DialogContent, FormControl} from "@mui/material";
import StyledTextField from "../styledComponent/styledTextField";
import IconButton from '@mui/material/IconButton';
import InputAdornment from '@mui/material/InputAdornment';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import Alert from "@mui/material/Alert";

const Profile = () => {

    const [myself, setMyself] = useState({})
    const [openForm, setOpenForm] = useState(false)
    const [errorMessage, setErrorMessage] = useState("")

    useEffect(() => {
        fetchMyselfData()
    }, [])

    const fetchMyselfData = async () => {
        console.log("AAAAA")
        const response = await axios.get("http://localhost:8080/api/employee/myself")
        setMyself(response.data)
    }

    const getProfileImage = () => {
        const remainder = myself.id % 3;
        switch (remainder) {
            case 0:
                return profile1;
            case 1:
                return profile2;
            case 2:
                return profile3;
            default:
                return profile1;
        }
    };

    function handleOpenForm() {
        setOpenForm(true)
    }

    function handleClose(){
        setOpenForm(false)
    }

    const clear = e => {
        setErrorMessage("")
    };

    function PasswordForm(props) {
        const {onClose, open} = props;
        const [showPassword, setShowPassword] = useState(false);
        const [disableChange, setDisableChange] = useState(true)
        const [password, setPassword] = useState("")

        useEffect(() => {
            if(password.trim() && password.length >= 6) {
                setDisableChange(false)
            }else {
                setDisableChange(true)
            }

        }, [password]);

        const handleChange = async () => {
            handleClose()
            const response = await axios.put("http://localhost:8080/api/employee/change-password", {
                password: password
            })
            if (response.data.error) {
                setErrorMessage(response.data.error)
                setTimeout(() => setErrorMessage(""), 3500)
            }
        }

        const handleClickShowPassword = () => setShowPassword((show) => !show);

        const handleMouseDownPassword = (event) => {
            event.preventDefault();
        };

        return (
            <Dialog onClose={onClose} open={open}>
                <DialogContent>
                    <FormControl sx={{ m: 1, width: '25ch' }} id="outlined-basic" variant="outlined">
                        <StyledTextField
                            id="outlined-adornment-password"
                            type={showPassword ? 'text' : 'password'}
                            InputProps={{
                                endAdornment: (
                                    <InputAdornment position="end">
                                        <IconButton
                                            aria-label="toggle password visibility"
                                            onClick={handleClickShowPassword}
                                            onMouseDown={handleMouseDownPassword}
                                            edge="end"
                                        >
                                            {showPassword ? <VisibilityOff/> : <Visibility/>}
                                        </IconButton>
                                    </InputAdornment>
                                )
                            }}
                            label='Password'
                            onChange={(event) => {setPassword(event.target.value)}}
                        />
                        <StyledButton
                            variant="text"
                            sx={{width: '50%', alignSelf: 'center'}}
                            onClick={handleChange}
                            disabled={disableChange}
                        >Change</StyledButton>
                    </FormControl>
                </DialogContent>
            </Dialog>
        )
    }

    return (
        <section style={{ backgroundColor: '#eee', height: '93.5vh', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center'}}>
            <PasswordForm
                open={openForm}
                onClose={handleClose}
            />
            <MDBContainer style={{margin: '0 auto', maxWidth: '500px'}}>
                <MDBRow>
                    <MDBCol>
                        <MDBCard className="mb-4">
                            <MDBCardBody>
                                <MDBCardBody className="text-center">
                                    <MDBCardImage
                                        src={getProfileImage()}
                                        alt="avatar"
                                        className="rounded-circle"
                                        style={{ width: '130px', height: '130px', objectFit: 'cover' }}
                                    />
                                    <p style={{marginTop: '10px', marginBottom: '5px'}} className="text-muted">{myself.role ? myself.role.charAt(0).toUpperCase() + myself.role.toLowerCase().slice(1) : ""}</p>
                                    <p className="text-muted">Salary: {myself.salary}₴</p>
                                </MDBCardBody>
                                <MDBRow>
                                    <MDBCol sm="3">
                                        <MDBCardText>Surname</MDBCardText>
                                    </MDBCol>
                                    <MDBCol sm="9">
                                        <MDBCardText className="text-muted">{myself.surname}</MDBCardText>
                                    </MDBCol>
                                </MDBRow>
                                <hr style={{margin: '10px 0'}} />
                                <MDBRow>
                                    <MDBCol sm="3">
                                        <MDBCardText>Name</MDBCardText>
                                    </MDBCol>
                                    <MDBCol sm="9">
                                        <MDBCardText className="text-muted">{myself.name}</MDBCardText>
                                    </MDBCol>
                                </MDBRow>
                                <hr style={{margin: '10px 0'}}/>
                                <MDBRow>
                                    <MDBCol sm="3">
                                        <MDBCardText>Patronymic</MDBCardText>
                                    </MDBCol>
                                    <MDBCol sm="9">
                                        <MDBCardText className="text-muted">{myself.patronymic}</MDBCardText>
                                    </MDBCol>
                                </MDBRow>
                                <hr style={{margin: '10px 0'}}/>
                                <MDBRow>
                                    <MDBCol sm="3">
                                        <MDBCardText>Mobile</MDBCardText>
                                    </MDBCol>
                                    <MDBCol sm="9">
                                        <MDBCardText className="text-muted">{myself.phoneNumber}</MDBCardText>
                                    </MDBCol>
                                </MDBRow>
                                <hr style={{margin: '10px 0'}}/>
                                <MDBRow>
                                    <MDBCol sm="3">
                                        <MDBCardText>Birth Date</MDBCardText>
                                    </MDBCol>
                                    <MDBCol sm="9">
                                        <MDBCardText className="text-muted">{myself.dateOfBirth}</MDBCardText>
                                    </MDBCol>
                                </MDBRow>
                                <hr style={{margin: '10px 0'}}/>
                                <MDBRow>
                                    <MDBCol sm="3">
                                        <MDBCardText>Start Date</MDBCardText>
                                    </MDBCol>
                                    <MDBCol sm="9">
                                        <MDBCardText className="text-muted">{myself.dateOfStart}</MDBCardText>
                                    </MDBCol>
                                </MDBRow>
                                <hr style={{margin: '10px 0'}}/>
                                <MDBRow>
                                    <MDBCol sm="3">
                                        <MDBCardText>City</MDBCardText>
                                    </MDBCol>
                                    <MDBCol sm="9">
                                        <MDBCardText className="text-muted">{myself.city}</MDBCardText>
                                    </MDBCol>
                                </MDBRow>
                                <hr style={{margin: '10px 0'}}/>
                                <MDBRow>
                                    <MDBCol sm="3">
                                        <MDBCardText>Street</MDBCardText>
                                    </MDBCol>
                                    <MDBCol sm="9">
                                        <MDBCardText className="text-muted">{myself.street}</MDBCardText>
                                    </MDBCol>
                                </MDBRow>
                                <hr style={{margin: '10px 0'}}/>
                                <MDBRow>
                                    <MDBCol sm="3">
                                        <MDBCardText>Zip Code</MDBCardText>
                                    </MDBCol>
                                    <MDBCol sm="9">
                                        <MDBCardText className="text-muted">{myself.zipCode}</MDBCardText>
                                    </MDBCol>
                                </MDBRow>
                            </MDBCardBody>
                        </MDBCard>
                    </MDBCol>
                </MDBRow>
            </MDBContainer>
            <StyledButton variant="outlined"
                          sx={{maxHeight:'40px', marginTop:'10px', marginRight: '20px'}}
                          onClick={handleOpenForm}
            >
                CHANGE PASSWORD
            </StyledButton>
            {errorMessage && <Alert style={{width: '40%', fontSize: '15px', position: 'fixed', right: '30%', top: '5%'}} severity="error" onClose={clear}>{errorMessage}</Alert>}
        </section>
    )

}

export default Profile