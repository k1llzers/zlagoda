import {useEffect, useState} from "react";
import {useAuth} from "../provider/authProvider";
import axios from "axios";
import * as React from "react";
import TableContainer from "@mui/material/TableContainer";
import Card from "@mui/material/Card";
import Table from "@mui/material/Table";
import TableHead from "@mui/material/TableHead";
import StyledTableRow from "../styledComponent/styledTableRow";
import StyledTableCell from "../styledComponent/styledTableCell";
import TableBody from "@mui/material/TableBody";
import TableRow from "@mui/material/TableRow";
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import {
    Accordion, AccordionDetails, AccordionSummary,
    Box,
    Button,
    Dialog,
    DialogContent,
    FormControl,
    IconButton,
    Paper,
    Stack,
    styled,
    Typography
} from "@mui/material";
import ReceiptLongIcon from '@mui/icons-material/ReceiptLong';
import {Container} from "react-bootstrap";
import Alert from "@mui/material/Alert";
import dayjs from "dayjs";
import Autocomplete from "@mui/material/Autocomplete";
import StyledTextField from "../styledComponent/styledTextField";
import StyledButton from "../styledComponent/styldButton";
import CustomerCards from "./CustomerCards";
import TableCell from "@mui/material/TableCell";

const Checks = () => {
    const [checks, setChecks] = useState([])
    const {role} = useAuth()
    const [errorMessage, setErrorMessage] = useState("")
    const [row, setRow] = useState(undefined)
    const [openForm, setOpenForm] = useState(false)
    const [openInfo, setOpenInfo] = useState(false)
    const [search, setSearch] = useState("")
    const [cashier, setCashier] = useState(0)
    const [dateFrom, setDateFrom] = useState(dayjs(new Date('2023-03-19T01:52:26.000')))
    const [dateTo, setDateTo] = useState(dayjs(new Date('2025-03-19T01:52:26.000')))


    useEffect(() => {
        fetchChecksData()
    }, [])

    const fetchChecksData = async () => {
        let response
        response = await axios.get("http://localhost:8080/api/check/by-employee?from=" + dateFrom.format('YYYY-MM-DDTHH:mm:ss.SSS00') + "&to=" + dateTo.format('YYYY-MM-DDTHH:mm:ss.SSS00'))
        setChecks(response.data)
    }

    const fetchSearchingCheckData = async (id) => {
        const response = await axios.get("http://localhost:8080/api/check/" + id)
        if(response.data.error)
            setChecks([])
        else
            setChecks([response.data])
    }

    function processEmployee(employee) {
        return {
            id: employee.id,
            surname: employee.surname,
            name: employee.name,
            patronymic: employee.patronymic,
            salary: employee.salary,
            dateOfBirth: employee.dateOfBirth,
            dateOfStart: employee.dateOfStart,
            phoneNumber: employee.phoneNumber,
            role: employee.role.charAt(0).toUpperCase() + employee.role.toLowerCase().slice(1),
            city: employee.city,
            street: employee.street,
            zipCode: employee.zipCode
        }
    }

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

    function processChecks(check) {
        return {
            id: check.id,
            printDate: check.printDate,
            sumTotal: check.sumTotal,
            vat: check.vat,
            employee: processEmployee(check.employee),
            customerCard: check.customerCard ? processCards(check.customerCard) : "",
            products: check.sales
        }
    }

    const columns = [
        {field: 'id', headerName: 'Check Number'},
        {field: 'printDate', headerName: 'Print Date'},
        {field: 'sumTotal', headerName: 'Sum Total'},
    ]

    const rows = checks.map((check) => processChecks(check))

    const clear = e => {
        setErrorMessage("")
    };

    function handleOpenForm() {
        setOpenForm(true)
    }

    function handleClose(){
        setOpenForm(false)
    }

    function handleCloseInfo() {
        setOpenInfo(false)
    }

    const handleSearch = async (e) => {
        setSearch(e.target.value)
        if (e.target.value.trim().length > 0 && !isNaN(e.target.value))
            await fetchSearchingCheckData(e.target.value)
        else
            await fetchChecksData()
    }

    function handleInfo(row) {
        setRow(row)
        setOpenInfo(true)
    }

    function Info(props) {
        const {onClose, open, row} = props;

        const Div = styled('div')(({ theme }) => ({
            ...theme.typography.button,
            backgroundColor: theme.palette.background.paper,
            padding: theme.spacing(1),
            fontSize: '19px',
            textAlign: 'center',
            margin: '10px 0'
        }));

        const P = styled('p')(() => ({
            fontSize: '17px'
        }))

        const Label = styled('span')(() => ({
            fontWeight: 'bold',
            color: '#748c8d'
        }))

        if (row !== undefined)
            return (
                <Dialog onClose={onClose} open={open} maxWidth='xs' fullWidth>
                    <DialogContent>
                        <Div>Check №{row.id}</Div>
                        <P><Label>Print Date: </Label>{new Date(row.printDate).toLocaleString()}</P>
                        <P><Label>Sum Total: </Label>{row.sumTotal} ₴</P>
                        <P><Label>Vat: </Label>{row.vat} ₴</P>
                        <Accordion sx={{fontSize: '17px', marginBottom: '10px'}}>
                            <AccordionSummary
                                expandIcon={<ExpandMoreIcon />}
                                aria-controls="panel1-content"
                                id="panel1-header"
                            >
                               Employee
                            </AccordionSummary>
                            <AccordionDetails>
                                <P><Label>Name: </Label>{row.employee.surname} {row.employee.name} {row.employee.patronymic}</P>
                                <P><Label>Phone Number: </Label>{row.employee.phoneNumber}</P>
                            </AccordionDetails>
                        </Accordion>
                        {row.customerCard && <Accordion sx={{fontSize: '17px'}}>
                            <AccordionSummary
                                expandIcon={<ExpandMoreIcon />}
                                aria-controls="panel1-content"
                                id="panel1-header"
                            >
                                Customer
                            </AccordionSummary>
                            <AccordionDetails>
                                <P><Label>Name: </Label>{row.customerCard.surname} {row.customerCard.name} {row.customerCard.patronymic}</P>
                                <P><Label>Phone Number: </Label>{row.customerCard.phoneNumber}</P>
                                <P><Label>Percent: </Label>{row.customerCard.percent} %</P>
                            </AccordionDetails>
                        </Accordion>}
                        <Div>Products</Div>
                        <TableContainer component={Paper}>
                            <Table sx={{ minWidth: 200 }} size="small" aria-label="a dense table">
                                <TableHead>
                                    <TableRow sx={{backgroundColor: '#748c8d'}}>
                                        <TableCell sx={{color: 'white'}} align="center">Product</TableCell>
                                        <TableCell sx={{color: 'white'}} align="center">Number</TableCell>
                                        <TableCell sx={{color: 'white'}} align="center">Price</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {row.products.map((row) => (
                                        <TableRow
                                            key={row.name}
                                            sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                                        >
                                            <TableCell align="center">{row.name}</TableCell>
                                            <TableCell align="center">{row.productNumber}</TableCell>
                                            <TableCell align="center">{row.sellingPrice}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </DialogContent>
                </Dialog>
            )
        else
            return;
    }

    function Row(props) {
        const {row} = props;
        return (
            <React.Fragment>
                <TableRow sx={{'& > *': {borderBottom: 'unset'}}}>
                    <StyledTableCell component="th" scope="row " align="center">{row.id}</StyledTableCell>
                    <StyledTableCell component="th" scope="row " align="center">{new Date(row.printDate).toLocaleString()}</StyledTableCell>
                    <StyledTableCell component="th" scope="row " align="center">{row.sumTotal} ₴</StyledTableCell>
                    <StyledTableCell align="right">
                        <Button onClick={() => handleInfo(row)}>
                            <ReceiptLongIcon color="action"/>
                        </Button>
                    </StyledTableCell>
                </TableRow>
            </React.Fragment>
        );
    }

    function ChecksTable() {
        return (
            <React.Fragment>
                <TableContainer component={Card} sx={{ maxWidth: 700, margin: '30px auto' }}>
                    <Table>
                        <TableHead>
                            <StyledTableRow>
                                {columns.map((column) => (
                                    <StyledTableCell align="center" key={column.field}>
                                        {column.headerName}
                                    </StyledTableCell>
                                ))}
                            <StyledTableCell/>
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
            <Info
                open={openInfo}
                onClose={handleCloseInfo}
                row={row}
            />
            <Box sx={{maxWidth: 900, margin: '0 auto'}}>
                {errorMessage && <Alert style={{width: '40%', fontSize: '15px', position: 'fixed', right: '30%', top: '5%'}} severity="error" onClose={clear}>{errorMessage}</Alert>}
                <ChecksTable/>
            </Box>
        </Container>
    );


}

export default Checks;