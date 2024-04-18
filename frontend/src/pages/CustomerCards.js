import * as React from 'react';
import {useEffect, useState} from "react";
import axios from "axios";
import TableContainer from "@mui/material/TableContainer";
import Card from "@mui/material/Card";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import StyledTableRow from "../styledComponent/styledTableRow";
import StyledTableCell from "../styledComponent/styledTableCell";
import TableBody from "@mui/material/TableBody";
import TableRow from "@mui/material/TableRow";
import {Box, Button, Dialog, DialogContent, FormControl, Grid, IconButton, Stack, Typography} from "@mui/material";
import KeyboardArrowUpIcon from "@mui/icons-material/KeyboardArrowUp";
import KeyboardArrowDownIcon from "@mui/icons-material/KeyboardArrowDown";
import Collapse from "@mui/material/Collapse";
import {Container} from "react-bootstrap";
import {useAuth} from "../provider/authProvider";
import ModeEditIcon from "@mui/icons-material/ModeEdit";
import DeleteOutlineOutlinedIcon from "@mui/icons-material/DeleteOutlineOutlined";
import StyledTextField from "../styledComponent/styledTextField";
import StyledButton from "../styledComponent/styldButton";
import Alert from "@mui/material/Alert";
import AddIcon from "@mui/icons-material/Add";
import SearchContainer from "../styledComponent/searchContainer";
import SearchIconWrapper from "../styledComponent/searchIconWrapper";
import SearchIcon from "@mui/icons-material/Search";
import StyledInputBase from "../styledComponent/styledInputBase";


const CustomerCards = () => {
    const [cards, setCards] = useState([])
    const {role} = useAuth()
    const [errorMessage, setErrorMessage] = useState("")
    const [openForm, setOpenForm] = useState(false)
    const [updateRow, setUpdateRow] = useState(undefined)
    const [cashierSearch, setCashierSearch] = useState("")
    const [managerSearch, setManagerSearch] = useState("")

    const fetchCardsData = async () => {
        const response = await axios.get("http://localhost:8080/api/customer-card/order-by/surname")
        setCards(response.data);
    };

    const fetchSearchingCardsDataCashier = async (surname) => {
        const response = await axios.get("http://localhost:8080/api/customer-card/by-surname?surname=" + surname)
        setCards(response.data);
    };

    const fetchSearchingCardsDataManager = async (percent) => {
        const response = await axios.get("http://localhost:8080/api/customer-card/order-by/surname/" + percent)
        setCards(response.data);
    };

    useEffect(() => {
        fetchCardsData();
    }, []);

    function processCards(card) {
        return {
            id: card.id,
            surname: card.surname,
            name: card.name,
            patronymic: card.patronymic,
            phoneNumber: card.phoneNumber,
            city: card.city,
            street: card.street,
            zipCode: card.zipCode,
            percent: card.percent
        }
    }

    const columns = [
        {field: 'surname', headerName: 'Surname'},
        {field: 'name', headerName: 'Name'},
        {field: 'patronymic', headerName: 'Patronymic'},
        {field: 'phoneNumber', headerName: 'Phone Number'},
        {field: 'percent', headerName: 'Percent'}
    ];

    console.log(cards)
    const rows = cards.map((card) => processCards(card));

    const handleDelete = async (cardId) => {
        const response = await axios.delete("http://localhost:8080/api/customer-card/" + cardId)
        if (response.data.error) {
            setErrorMessage("Can't delete customer card that present at at least one check")
            setTimeout(() => setErrorMessage(""), 3500)
        } else {
            if (response.data === true)
                setCards(cards.filter(card => card.id !== cardId));
        }
    }

    const clear = e => {
        setErrorMessage("")
    };

    function handleOpenForm() {
        setOpenForm(true)
    }

    function handleUpdate(row) {
        setUpdateRow(row)
        handleOpenForm()
    }

    function handleClose(){
        setOpenForm(false)
        setUpdateRow(undefined)
    }

    const handleSearchCashier = async (e) => {
        setCashierSearch(e.target.value)
        if (e.target.value.trim().length > 0)
            await fetchSearchingCardsDataCashier(e.target.value)
        else
            await fetchCardsData()
    };

    const handleSearchManager = async (e) => {
        setManagerSearch(e.target.value)
        if (e.target.value.trim().length > 0 && !isNaN(e.target.value))
            await fetchSearchingCardsDataManager(e.target.value)
        else
            await fetchCardsData()
    };


    function CardForm(props) {
        const {onClose, open, row} = props;
        const [surname, setSurname] = useState(row ? row.surname : "")
        const [name, setName] = useState(row ? row.name : "")
        const [patronymic, setPatronymic] = useState(row ? row.patronymic : "")
        const [phoneNumber, setPhoneNumber] = useState(row ? row.phoneNumber : "+380")
        const [city, setCity] = useState(row ? row.city : "")
        const [street, setStreet] = useState(row ? row.street : "")
        const [zipCode, setZipCode] = useState(row ? row.zipCode : "")
        const [percent, setPercent] = useState(row ? row.percent : "")
        const [disableAdd, setDisableAdd] = useState(true)

        const handleAdd = async () => {
            handleClose()
            let response;
            if(!row) {
                response = await axios.post("http://localhost:8080/api/customer-card", {
                    surname: surname,
                    name: name,
                    patronymic: patronymic,
                    phoneNumber: phoneNumber,
                    city: city,
                    street: street,
                    zipCode: zipCode,
                    percent: +percent
                })
            } else {
                response = await axios.put("http://localhost:8080/api/customer-card", {
                    id: row.id,
                    surname: surname,
                    name: name,
                    patronymic: patronymic,
                    phoneNumber: phoneNumber,
                    city: city,
                    street: street,
                    zipCode: zipCode,
                    percent: +percent
                })
            }
            if (response.data.error) {
                setErrorMessage(response.data.error)
                setTimeout(() => setErrorMessage(""), 3500)
            } else {
                fetchCardsData()
            }
        }

        useEffect(() => {
            if(surname.trim()
                && name.trim()
                && patronymic.trim()
                && phoneNumber.trim()
                && city.trim()
                && street.trim()
                && zipCode.trim()
                && surname.length <= 50
                && name.length <= 50
                && patronymic.length <= 50
                && city.length <= 50
                && street.length <= 50
                && zipCode.length <= 9
                && phoneNumber.length <= 13
                && check.test(phoneNumber)
                && !isNaN(+percent)
                && +percent <= 100
            ) {
                setDisableAdd(false)
            }else {
                setDisableAdd(true)
            }

        }, [surname, name, patronymic, phoneNumber, city, street, zipCode, percent]);


        const check = /^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{6}$/im

        return (
            <Dialog onClose={onClose} open={open}>
                <DialogContent>
                    <FormControl fullWidth>
                        <Box sx={{flexGrow: 1}}>
                            <Grid container>
                                <Grid item xs={6}>
                                    <StyledTextField sx={{width:'93%'}} id="outlined-basic" label="Surname" variant="outlined" value={surname}
                                                     required
                                                     error={surname.length > 50}
                                                     helperText={surname.length > 50 ? "Too long" : ""}
                                                     onChange={(event) => {setSurname(event.target.value)}}
                                    />
                                </Grid>
                                <Grid item xs={6}>
                                    <StyledTextField sx={{width:'93%'}} id="outlined-basic" label="Name" variant="outlined" value={name}
                                                     required
                                                     error={name.length > 50}
                                                     helperText={name.length > 50 ? "Too long" : ""}
                                                     onChange={(event) => {setName(event.target.value)}}
                                    />
                                </Grid>
                                <Grid item xs={6}>
                                    <StyledTextField sx={{width:'93%'}} id="outlined-basic" label="Patronymic" variant="outlined" value={patronymic}
                                                     required
                                                     error={patronymic.length > 50}
                                                     helperText={patronymic.length > 50 ? "Too long" : ""}
                                                     onChange={(event) => {setPatronymic(event.target.value)}}
                                    />
                                </Grid>
                                <Grid item xs={6}>
                                    <StyledTextField sx={{width:'93%'}} id="outlined-basic" label="Phone Number" variant="outlined" value={phoneNumber}
                                                     required
                                                     error={phoneNumber.length > 13 || !check.test(phoneNumber)}
                                                     helperText={phoneNumber.length > 13 || !check.test(phoneNumber) ? "Incorrect number" : ""}
                                                     onChange={(event) => {setPhoneNumber(event.target.value)}}
                                    />
                                </Grid>
                                <Grid item xs={6}>
                                    <StyledTextField sx={{width:'93%'}} id="outlined-basic" label="City" variant="outlined" value={city}
                                                     required
                                                     error={city.length > 50}
                                                     helperText={city.length > 50 ? "Too long" : ""}
                                                     onChange={(event) => {setCity(event.target.value)}}
                                    />
                                </Grid>
                                <Grid item xs={6}>
                                    <StyledTextField sx={{width:'93%'}} id="outlined-basic" label="Street" variant="outlined" value={street}
                                                     required
                                                     error={street.length > 50}
                                                     helperText={street.length > 50 ? "Too long" : ""}
                                                     onChange={(event) => {setStreet(event.target.value)}}
                                    />
                                </Grid>
                                <Grid item xs={6}>
                                    <StyledTextField sx={{width:'93%'}} id="outlined-basic" label="Zip Code" variant="outlined" value={zipCode}
                                                     required
                                                     error={zipCode.length > 9}
                                                     helperText={zipCode.length > 9 ? "Too long" : ""}
                                                     onChange={(event) => {setZipCode(event.target.value)}}
                                    />
                                </Grid>
                                <Grid item xs={6}>
                                    <StyledTextField sx={{width:'93%'}} id="outlined-basic" label="Percent" variant="outlined" value={percent}
                                                     required
                                                     error={isNaN(+percent) || +percent > 100 }
                                                     helperText={isNaN(+percent) || +percent > 100 ? "Incorrect percent" : ""}
                                                     onChange={(event) => {setPercent(event.target.value)}}
                                    />
                                </Grid>
                            </Grid>
                        </Box>

                        <StyledButton
                            variant="text"
                            sx={{width: '50%', alignSelf: 'center'}}
                            onClick={handleAdd}
                            disabled={disableAdd}
                        >{row ? "Update" : "Add"}</StyledButton>
                    </FormControl>
                </DialogContent>
            </Dialog>
        )
    }

    function Row(props) {
        const {row} = props;
        const [open, setOpen] = useState(false);

        return (
            <React.Fragment>
                <TableRow sx={{'& > *': {borderBottom: 'unset'}}}>
                    <StyledTableCell>
                        <IconButton
                            aria-label="expand row"
                            size="small"
                            onClick={() => setOpen(!open)}>
                            {open ? <KeyboardArrowUpIcon/> : <KeyboardArrowDownIcon/>}
                        </IconButton>
                    </StyledTableCell>
                    <StyledTableCell component="th" scope="row " align="center">{row.surname}</StyledTableCell>
                    <StyledTableCell align="center">{row.name}</StyledTableCell>
                    <StyledTableCell align="center">{row.patronymic}</StyledTableCell>
                    <StyledTableCell align="center">{row.phoneNumber}</StyledTableCell>
                    <StyledTableCell align="center">{row.percent} %</StyledTableCell>
                    <StyledTableCell align="center">
                        <Button onClick={() => handleUpdate(row)}>
                            <ModeEditIcon color='action'/>
                        </Button>
                        <Button onClick={() => handleDelete(row.id)}>
                            <DeleteOutlineOutlinedIcon color="error"/>
                        </Button>
                    </StyledTableCell>
                </TableRow>
                <TableRow>
                    <StyledTableCell style={{paddingBottom: 0, paddingTop: 0}} colSpan={7}>
                        <Collapse in={open} timeout="auto" unmountOnExit>
                            <Box sx={{margin: 1}}>
                                <Typography variant="span" gutterBottom component="div" style={{fontSize: 18}}>
                                    Address
                                </Typography>
                                {row.city}, {row.street}, {row.zipCode}
                            </Box>
                        </Collapse>
                    </StyledTableCell>
                </TableRow>
            </React.Fragment>
        );
    }


    function ProductsTable() {
        return (
            <React.Fragment>
                <TableContainer component={Card} sx={{ maxWidth: 1000, margin: '30px auto' }}>
                    <Table aria-label="collapsible table">
                        <TableHead>
                            <StyledTableRow>
                                <StyledTableCell/>
                                {columns.map((column) => (
                                    <StyledTableCell align="center" key={column.field} sx={{width:"16%"}}>
                                        {column.headerName}
                                    </StyledTableCell>
                                ))}
                                <StyledTableCell sx={{width:"20%"}} />
                            </StyledTableRow>
                        </TableHead>
                        <TableBody>
                            {rows.map((row) => (
                                <Row key={row.name} row={row} />
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </React.Fragment>
        )
    }

    return (
        <Container style={{ marginTop: '50px'}}>
            <CardForm
                open={openForm}
                onClose={handleClose}
                row={updateRow}
            />
            <Box sx={{maxWidth: 1000, margin: '0 auto'}}>
                <Stack direction='row' justifyContent='space-between'>
                    {role === 'MANAGER' &&
                        <StyledButton variant="outlined"
                                      startIcon={<AddIcon />}
                                      onClick={handleOpenForm}
                                      sx={{maxHeight:'40px', marginTop:'10px'}}
                        >
                            Add
                        </StyledButton>}
                    {role === "CASHIER" &&
                        <SearchContainer sx={{maxHeight:'40px', marginTop:'10px'}}>
                            <SearchIconWrapper>
                                <SearchIcon />
                            </SearchIconWrapper>
                            <StyledInputBase
                                placeholder="Surname…"
                                value={cashierSearch}
                                inputProps={{ 'aria-label': 'search' }}
                                onChange={handleSearchCashier}
                            />
                        </SearchContainer>
                    }
                    {role === "MANAGER" &&
                        <SearchContainer sx={{maxHeight:'40px', marginTop:'10px'}}>
                            <SearchIconWrapper>
                                <SearchIcon />
                            </SearchIconWrapper>
                            <StyledInputBase
                                placeholder="Percent…"
                                value={managerSearch}
                                inputProps={{ 'aria-label': 'search' }}
                                onChange={handleSearchManager}
                            />
                        </SearchContainer>
                    }
                </Stack>
                {errorMessage && <Alert style={{width: '40%', fontSize: '15px', position: 'fixed', right: '30%', top: '5%'}} severity="error" onClose={clear}>{errorMessage}</Alert>}
                <ProductsTable/>
            </Box>
        </Container>
    );
}

export default CustomerCards;