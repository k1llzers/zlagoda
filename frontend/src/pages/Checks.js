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
    IconButton, MenuItem,
    Paper,
    Stack,
    styled,
    Typography
} from "@mui/material";
import ReceiptLongIcon from '@mui/icons-material/ReceiptLong';
import {Container} from "react-bootstrap";
import Alert from "@mui/material/Alert";
import dayjs from "dayjs";
import StyledButton from "../styledComponent/styldButton";
import AddIcon from "@mui/icons-material/Add";
import CustomerCards from "./CustomerCards";
import TableCell from "@mui/material/TableCell";
import Autocomplete from "@mui/material/Autocomplete";
import StyledTextField from "../styledComponent/styledTextField";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import {DatePicker, LocalizationProvider} from "@mui/x-date-pickers";
import StyledLabel from "../styledComponent/styldLabel";
import StyledSelect from "../styledComponent/styledSelect";

const Checks = () => {
    const [checks, setChecks] = useState([])
    const {role} = useAuth()
    const [errorMessage, setErrorMessage] = useState("")
    const [row, setRow] = useState(undefined)
    const [openForm, setOpenForm] = useState(false)
    const [openInfo, setOpenInfo] = useState(false)
    const [customerCards, setCustomerCards] = useState([])
    const [storeProducts, setStoreProducts] = useState([])
    const [search, setSearch] = useState("")
    const [sum, setSum] = useState(0)
    const [number, setNumber] = useState(0)
    const [cashier, setCashier] = useState(0)
    const [cashiers, setCashiers] = useState([])
    const [dateFrom, setDateFrom] = useState(dayjs(new Date('2023-03-19T01:52:26.000')))
    const [dateTo, setDateTo] = useState(dayjs(new Date('2025-03-19T01:52:26.000')))
    const [products, setProducts] = useState([])
    const [product, setProduct] = useState(0);


    useEffect(() => {
        fetchChecksData()
        fetchCashierData()
        fetchSum()
    }, [dateTo, dateFrom, cashier])

    useEffect(() => {
        fetchProductsData()
    }, [])

    useEffect(() => {
        fetchNumber()
    }, [dateTo, dateFrom, product])

    const fetchChecksData = async () => {
        let response
        if (cashier === 0)
            response = await axios.get("http://localhost:8080/api/check/by-employee?from=" + dateFrom.format('YYYY-MM-DDTHH:mm:ss.SSS00') + "&to=" + dateTo.format('YYYY-MM-DDTHH:mm:ss.SSS00'))
        else
            response = await axios.get("http://localhost:8080/api/check/by-employee?empl=" + cashier + "&from=" + dateFrom.format('YYYY-MM-DDTHH:mm:ss.SSS00') + "&to=" + dateTo.format('YYYY-MM-DDTHH:mm:ss.SSS00'))
        setChecks(response.data)
    }

    const fetchSum = async () => {
        let response
        if (cashier === 0)
            response = await axios.get("http://localhost:8080/api/check/by-employee/products-sum?from=" + dateFrom.format('YYYY-MM-DDTHH:mm:ss.SSS00') + "&to=" + dateTo.format('YYYY-MM-DDTHH:mm:ss.SSS00'))
        else
            response = await axios.get("http://localhost:8080/api/check/by-employee/products-sum?empl=" + cashier + "&from=" + dateFrom.format('YYYY-MM-DDTHH:mm:ss.SSS00') + "&to=" + dateTo.format('YYYY-MM-DDTHH:mm:ss.SSS00'))
        setSum(response.data)
    }

    const fetchNumber = async () => {
        let response
        if (product !== 0) {
            response = await axios.get("http://localhost:8080/api/check/count/product/" + product + "?from=" + dateFrom.format('YYYY-MM-DDTHH:mm:ss.SSS00') + "&to=" + dateTo.format('YYYY-MM-DDTHH:mm:ss.SSS00'))
            setNumber(response.data)
        }

    }

    const fetchSearchingCheckData = async (id) => {
        const response = await axios.get("http://localhost:8080/api/check/" + id)
        if(response.data.error)
            setChecks([])
        else
            setChecks([response.data])
    }

    const fetchCashierData = async () => {
        const response = await axios.get("http://localhost:8080/api/employee/cashier/order-by/surname")
        setCashiers(response.data)
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

    async function handleOpenForm() {
        setOpenForm(true)
        await fetchCustomerCard()
        await fetchStoreProducts()
    }

    async function fetchCustomerCard() {
        const response = await axios.get("http://localhost:8080/api/customer-card")
        setCustomerCards(response.data)
    }

    async function fetchStoreProducts() {
        const response = await axios.get("http://localhost:8080/api/store-product")
        setStoreProducts(response.data)
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

    const P = styled('p')(() => ({
        fontSize: '17px'
    }))

    const Label = styled('span')(() => ({
        fontWeight: 'bold',
        color: '#748c8d'
    }))

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

        if (row !== undefined)
            return (
                <Dialog onClose={onClose} open={open} maxWidth='xs' fullWidth>
                    <DialogContent>
                        <Div>Check №{row.id}</Div>
                        <P><Label>Print Date: </Label>{new Date(row.printDate).toLocaleString()}</P>
                        <P><Label>Total Sum: </Label>{row.sumTotal} ₴</P>
                        <P><Label>Vat: </Label>{row.vat} ₴</P>
                        <Accordion sx={{fontSize: '17px', marginBottom: '10px'}}>
                            <AccordionSummary
                                expandIcon={<ExpandMoreIcon />}
                                aria-controls="panel1-content"
                                id="panel1-header"
                            >
                               Cashier
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
                                        <TableCell sx={{color: 'white'}} align="center">UPC</TableCell>
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
                                            <TableCell align="center">{row.id}</TableCell>
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

    function ChecksForm(props) {
        const {onClose, open} = props;
        const [customer, setCustomer] = useState("");
        const [storeProduct, setStoreProduct] = useState("");
        const [disableAdd, setDisableAdd] = useState(true)
        let checkProducts = []

        const customerCardOptions = customerCards.map(customerCard => ({
            label: customerCard.surname + " " + customerCard.name + " " + customerCard.patronymic + " " + customerCard.phoneNumber,
            value: customerCard.id
        }));

        const storeProductsOption = storeProducts.map(storeProduct => ({
            label: storeProduct.id + ": " + storeProduct.product.name + ", number: " + storeProduct.productsNumber,
            color: storeProduct.promotion ? "#0288d1" : "rgba(0, 0, 0, 1)",
            value: storeProduct
        }));

        useEffect(() => {
            if(checkProducts) {
                setDisableAdd(false)
            }else {
                setDisableAdd(true)
            }
        }, [customer, checkProducts, storeProduct]);

        const handleAddProduct = (event, newValue) => {
            if(newValue)
                setStoreProduct(newValue.value)
            else
                setStoreProduct("")
        }

        // const handleAdd = async () => {
        //     handleClose()
        //     let response;
        //     if(!row) {
        //         response = await axios.post("http://localhost:8080/api/store-product", {
        //             productId: +product,
        //             sellingPrice: sellingPrice,
        //             productsNumber: productsNumber,
        //             promotional: false
        //         })
        //     } else {
        //         response = await axios.put("http://localhost:8080/api/store-product", {
        //             id: row.id,
        //             sellingPrice: sellingPrice,
        //             productsNumber: productsNumber,
        //         })
        //     }
        //     if (response.data.error) {
        //         setErrorMessage(response.data.error)
        //         setTimeout(() => setErrorMessage(""), 3500)
        //     } else {
        //         fetchStoreProductsData()
        //     }
        // }


        return(
            <Dialog onClose={onClose} open={open} maxWidth='xs' fullWidth>
                <DialogContent>
                    <FormControl fullWidth>
                        <Autocomplete
                            value={customerCardOptions.find(option => option.value === customer)}
                            onChange = {(event, newValue) => {
                                if(newValue)
                                    setCustomer(newValue.value)
                                else
                                    setCustomer("")
                            }}
                            renderInput={(params) => <StyledTextField {...params} sx={{width: "100%"}} size="medium" label="Customer"/>}
                            options={customerCardOptions}
                            sx={{display:"flex"}}
                        />
                        <Autocomplete
                            value={storeProductsOption.find(option => option.value === storeProduct)}
                            onChange = {handleAddProduct}
                            renderInput={(params) => <StyledTextField {...params} sx={{width: "100%"}} size="medium" label="Product"/>}
                            renderOption={(props, option) => {
                                const { label, color } = option;
                                return (
                                    <span {...props} style={{ color: color }}>
                                        {label}
                                    </span>
                                );
                            }}
                            options={storeProductsOption}
                            sx={{display:"flex"}}
                        />
                        <TableContainer sx={{margin:'10px'}} component={Paper}>
                            <Table sx={{ minWidth: 200}} size="small" aria-label="a dense table">
                                <TableHead>
                                    <TableRow sx={{backgroundColor: '#748c8d'}}>
                                        <TableCell sx={{color: 'white'}} align="center">UPC</TableCell>
                                        <TableCell sx={{color: 'white'}} align="center">Product</TableCell>
                                        <TableCell sx={{color: 'white'}} align="center">Number</TableCell>
                                        <TableCell sx={{color: 'white'}} align="center">Price</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {checkProducts.map((product) => (
                                        <TableRow
                                            key={product.upc}
                                            sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                                        >
                                            <TableCell align="center">{product.upc}</TableCell>
                                            <TableCell align="center">{product.name}</TableCell>
                                            <TableCell align="center">{product.productNumber}</TableCell>
                                            <TableCell align="center"></TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                        <StyledButton
                            variant="text"
                            sx={{width: '50%', alignSelf: 'center'}}
                            // onClick={handleAdd}
                            disabled={disableAdd}
                        >Create</StyledButton>
                    </FormControl>
                </DialogContent>
            </Dialog>
        )
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

    const fetchProductsData = async () => {
        const response = await axios.get("http://localhost:8080/api/product/without/storeproduct")
        setProducts(response.data)
    };

    const productOptions = products.map(product => ({
        label: product.name,
        value: product.id
    }));

    return (
        <Container style={{ marginTop: '50px'}}>
            <Info
                open={openInfo}
                onClose={handleCloseInfo}
                row={row}
            />
            <ChecksForm
                open={openForm}
                onClose={handleClose}
            />
            <Box sx={{maxWidth: 700, margin: '0 auto'}}>
                <Stack direction='row' justifyContent='space-between' sx={{marginBottom: '20px'}}>
                    {role === 'CASHIER' &&
                        <StyledButton variant="outlined"
                                      startIcon={<AddIcon />}
                                      onClick={handleOpenForm}
                                      sx={{maxHeight:'40px', marginTop:'10px'}}
                        >
                            CREATE
                        </StyledButton>}
                </Stack>
                <Stack direction='row'>
                    <Stack direction='row' justifyContent='space-between' sx={{width: '45%'}}>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <DatePicker
                                label="From"
                                views={['year', 'month', 'day']}
                                value={dateFrom}
                                slotProps={{ textField: { size: 'small' } }}
                                sx={{width: '50%', marginRight: '10px'}}
                                onChange={(newValue) => setDateFrom(newValue)}
                            />
                        </LocalizationProvider>
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                            <DatePicker
                                label="To"
                                views={['year', 'month', 'day']}
                                value={dateTo}
                                slotProps={{ textField: { size: 'small' } }}
                                sx={{width: '50%'}}
                                onChange={(newValue) => setDateTo(newValue)}
                            />
                        </LocalizationProvider>
                    </Stack>
                    {role === "MANAGER" && <FormControl variant="outlined" size="small" sx={{maxHeight:'40px', minWidth:'120px'}}>
                        <StyledLabel variant="outlined" id="demo-simple-select-label" sx={{margin: '0 0 0 10px'}}>
                            Cashier
                        </StyledLabel>
                        <StyledSelect sx={{maxHeight:'40px', minWidth:'120px', margin:'0 0 0 10px'}}
                                      labelId="demo-simple-select-label"
                                      id="demo-simple-select"
                                      label="Cashier"
                                      value={cashier}
                                      onChange={(event) => {setCashier(event.target.value); setSearch("")}}
                        >
                            {cashiers.map((cashier) => (
                                <MenuItem
                                    key={cashier.id}
                                    value={cashier.id}
                                >{cashier.surname} {cashier.name} {cashier.patronymic} {cashier.phoneNumber}</MenuItem>
                            ))}
                            <MenuItem key={0} value={0}>All</MenuItem>
                        </StyledSelect>
                    </FormControl>}
                </Stack>
                {errorMessage && <Alert style={{width: '40%', fontSize: '15px', position: 'fixed', right: '30%', top: '5%'}} severity="error" onClose={clear}>{errorMessage}</Alert>}
                <ChecksTable/>
                {role === "MANAGER" && <P><Label>Total Sum: </Label>{sum} ₴</P>}
                {role === "MANAGER" &&
                    <Autocomplete
                        value={productOptions.find(option => option.value === product)}
                        onChange = {(event, newValue) => {
                            if(newValue)
                                setProduct(newValue.value)
                            else
                                setProduct(0)
                        }}
                        renderInput={(params) => <StyledTextField {...params} sx={{width: "100%"}} size="medium" label="Product"/>}
                        options={productOptions}
                        sx={{display:"flex"}}
                    />}
            </Box>
        </Container>
    );


}

export default Checks;