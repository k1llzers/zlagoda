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
import {
    Box,
    Button,
    Dialog,
    DialogContent,
    FormControl,
    Grid,
    IconButton, Paper,
    Stack,
    styled,
    Typography
} from "@mui/material";
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
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import TableCell from "@mui/material/TableCell";


const CustomerCards = () => {
    const [cards, setCards] = useState([])
    const {role} = useAuth()
    const [errorMessage, setErrorMessage] = useState("")
    const [openForm, setOpenForm] = useState(false)
    const [updateRow, setUpdateRow] = useState(undefined)
    const [cashierSearch, setCashierSearch] = useState("")
    const [managerSearch, setManagerSearch] = useState("")
    const [topCategories, setTopCategories] = useState([])
    const [openCategoryPopup, setOpenCategoryPopup] = useState(false)

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

    const fetchTopCategoryData = async (row) => {
        const response = await axios.get("http://localhost:8080/api/category/top/by-customer-card/" + row.id);
        setTopCategories(response.data)
    }

    function handleOpenTopCategoryPopup(row) {
        fetchTopCategoryData(row)
        setUpdateRow(row)
        setOpenCategoryPopup(true)
    }

    function handleClose(){
        setOpenForm(false)
        setUpdateRow(undefined)
    }

    function handleClosePopup() {
        setOpenCategoryPopup(false)
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

    function TopCategoryPopup(props) {
        const {onClose, open, row} = props;

        const P = styled('p')(() => ({
            fontSize: '17px'
        }))

        const Label = styled('span')(() => ({
            fontWeight: 'bold',
            color: '#748c8d'
        }))

        const Div = styled('div')(({ theme }) => ({
            ...theme.typography.button,
            backgroundColor: theme.palette.background.paper,
            padding: theme.spacing(1),
            fontSize: '16px',
            textAlign: 'center',
            margin: '5px 0'
        }));

        return (
            <Dialog onClose={onClose} open={open}>
                <DialogContent>
                    <P><Label>Customer: </Label>{row ? row.surname : ""} {row ? row.name : ""} {row ? row.patronymic ? row.patronymic : "" : ""}</P>
                    <Div>Top categories</Div>
                    <TableContainer component={Paper} sx={{maxHeight: 150, overflowY: 'auto' }}>
                        <Table sx={{ minWidth: 200}} stickyHeader size="small" aria-label="a dense table">
                            <TableHead>
                                <TableRow sx={{backgroundColor: '#748c8d'}}>
                                    <TableCell sx={{color: 'white', backgroundColor: '#748c8d'}} align="center">Category</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {topCategories.map((category) => (
                                    <TableRow
                                        key={category.id}
                                        sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                                    >
                                        <TableCell align="center">{category.name}</TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </DialogContent>
            </Dialog>
        )
    }


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
                    patronymic: patronymic ? patronymic : null,
                    phoneNumber: phoneNumber,
                    city: city ? city : null,
                    street: street ? street : null,
                    zipCode: zipCode ? zipCode : null,
                    percent: +percent
                })
            } else {
                response = await axios.put("http://localhost:8080/api/customer-card", {
                    id: row.id,
                    surname: surname,
                    name: name,
                    patronymic: patronymic ? patronymic : null,
                    phoneNumber: phoneNumber,
                    city: city ? city : null,
                    street: street ? street : null,
                    zipCode: zipCode ? zipCode : null,
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
                && phoneNumber.trim()
                && percent
                && surname.length <= 50
                && name.length <= 50
                && (!patronymic || patronymic.length <= 50)
                && (!city || city.length <= 50)
                && (!street || street.length <= 50)
                && (!zipCode || zipCode.length <= 9)
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
                                                     error={patronymic ? (patronymic.length > 50) : false}
                                                     helperText={patronymic ? (patronymic.length > 50 ? "Too long" : "") : false}
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
                                                     error={city ? (city.length > 50) : false}
                                                     helperText={city ? (city.length > 50 ? "Too long" : "") : false}
                                                     onChange={(event) => {setCity(event.target.value)}}
                                    />
                                </Grid>
                                <Grid item xs={6}>
                                    <StyledTextField sx={{width:'93%'}} id="outlined-basic" label="Street" variant="outlined" value={street}
                                                     error={street ? (street.length > 50) : false}
                                                     helperText={street ? (street.length > 50 ? "Too long" : "") : false}
                                                     onChange={(event) => {setStreet(event.target.value)}}
                                    />
                                </Grid>
                                <Grid item xs={6}>
                                    <StyledTextField sx={{width:'93%'}} id="outlined-basic" label="Zip Code" variant="outlined" value={zipCode}
                                                     error={zipCode ? (zipCode.length > 9) : false}
                                                     helperText={zipCode ? (zipCode.length > 9 ? "Too long" : "") : false}
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
                        {role === "MANAGER" && <Button onClick={() => handleDelete(row.id)}>
                            <DeleteOutlineOutlinedIcon color="error"/>
                        </Button>}
                        {role === "MANAGER" && <Button onClick={() => handleOpenTopCategoryPopup(row)}>
                            <TrendingUpIcon color="#455a64"/>
                        </Button>}
                    </StyledTableCell>
                </TableRow>
                <TableRow>
                    <StyledTableCell style={{paddingBottom: 0, paddingTop: 0}} colSpan={7}>
                        <Collapse in={open} timeout="auto" unmountOnExit>
                            <Box sx={{margin: 1}}>
                                <Typography variant="span" gutterBottom component="div" style={{fontSize: 18}}>
                                    Address
                                </Typography>
                                {row.city} {row.street} {row.zipCode}
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
                <TableContainer component={Card} sx={{ maxWidth: 1100, margin: '30px auto', maxHeight: '60vh', overflowY: 'auto' }}>
                    <Table stickyHeader aria-label="collapsible table">
                        <TableHead>
                            <StyledTableRow>
                                <StyledTableCell/>
                                {columns.map((column) => (
                                    <StyledTableCell align="center" key={column.field} sx={{width:"15%"}}>
                                        {column.headerName}
                                    </StyledTableCell>
                                ))}
                                <StyledTableCell sx={{width:"25%"}} />
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
            <TopCategoryPopup
                open={openCategoryPopup}
                onClose={handleClosePopup}
                row={updateRow}
            />
            <Box sx={{maxWidth: 1100, margin: '0 auto'}}>
                <Stack direction='row' justifyContent='space-between'>
                        <StyledButton variant="outlined"
                                      startIcon={<AddIcon />}
                                      onClick={handleOpenForm}
                                      sx={{maxHeight:'40px', marginTop:'10px'}}
                        >
                            Add
                        </StyledButton>
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
                {role === 'MANAGER' &&
                    <Stack direction='row' justifyContent='center'>
                        <a href="http://localhost:8080/report/CUSTOMER_CARDS" target="_blank">
                            <StyledButton variant="outlined" sx={{maxHeight:'40px', marginTop:'10px'}}>
                                PRINT REPORT
                            </StyledButton>
                        </a>
                    </Stack>
                }
            </Box>
        </Container>
    );
}

export default CustomerCards;